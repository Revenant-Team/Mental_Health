import Post from '../Models/postModel.js';
import Reply from '../Models/replyModel.js';
import Upvote from '../Models/upvoteModel.js';
import { generateAnonymousId } from '../utlis/anonymousId.js';

// GET /api/forum/posts
export const getAllPosts = async (req, res) => {
  try {
    const {
      page = 1,
      limit = 20,
      category,
      sortBy = 'newest',
      instituteId
    } = req.query;    

    const skip = (page - 1) * limit;
    let sortOption = {};

    switch (sortBy) {
      case 'oldest':
        sortOption = { 'metadata.createdAt': 1 };
        break;
      case 'mostLiked':
        sortOption = { 'engagement.upvotes': -1 };
        break;
      case 'mostViewed':
        sortOption = { 'engagement.views': -1 };
        break;
      default:
        sortOption = { 'metadata.createdAt': -1 };
    }

    const filter = { 'metadata.isActive': true };
    if (category) filter.category = category;
    if (instituteId) filter.instituteId = instituteId;

    const posts = await Post.aggregate([
      { $match: filter },
      { $sort: sortOption },
      { $skip: parseInt(skip) },
      { $limit: parseInt(limit) },
      {
        $lookup: {
          from: 'institutes',
          localField: 'instituteId',
          foreignField: '_id',
          as: 'institute'
        }
      },
      { $unwind: '$institute' },
      {
        $project: {
          _id: 1,
          title: 1,
          content: 1,
          category: 1,
          tags: 1,
          'engagement.upvotes': 1,
          'engagement.totalReplies': 1,
          'engagement.views': 1,
          'metadata.createdAt': 1,
          'metadata.priority': 1,
          'institute.name': 1,
          anonymousId: 1
        }
      }
    ]);

    const total = await Post.countDocuments(filter);

    res.json({
      success: true,
      data: {
        posts,
        pagination: {
          current: parseInt(page),
          pages: Math.ceil(total / limit),
          total
        }
      }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error fetching posts'
    });
  }
};

// POST /api/forum/posts
export const createPost = async (req, res) => {
  try {
    const { title, content, category, tags, priority } = req.body;
    const userId = req.user.id;
    const anonymousId = generateAnonymousId(userId);

    const post = new Post({
      anonymousId,
      instituteId: req.user.instituteId,
      title,
      content,
      category,
      tags: tags || [],
      metadata: {
        priority: priority || 'Medium'
      }
    });

    await post.save();

    res.status(201).json({
      success: true,
      message: 'Post created successfully',
      data: { post }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error creating post'
    });
  }
};

// GET /api/forum/posts/:postId
export const getPostById = async (req, res) => {
  try {
    const { postId } = req.params;

    const post = await Post.findById(postId)
      .populate('instituteId', 'name code')
      .lean();

    if (!post || !post.metadata.isActive) {
      return res.status(404).json({
        success: false,
        message: 'Post not found'
      });
    }

    // Check if this is user's own post
    let isMyPost = false;
    if (req.user) {
      const userAnonymousId = generateAnonymousId(req.user.id);
      isMyPost = post.anonymousId === userAnonymousId;
    }

    res.json({
      success: true,
      data: {
        post: {
          ...post,
          isMyPost
        }
      }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error fetching post'
    });
  }
};

// PUT /api/forum/posts/:postId
export const updatePost = async (req, res) => {
  try {
    const { postId } = req.params;
    const { title, content, category, tags } = req.body;
    const userId = req.user.id;
    const userAnonymousId = generateAnonymousId(userId);

    const post = await Post.findById(postId);
    if (!post) {
      return res.status(404).json({
        success: false,
        message: 'Post not found'
      });
    }

    // Check ownership
    if (post.anonymousId !== userAnonymousId) {
      return res.status(403).json({
        success: false,
        message: 'Not authorized to update this post'
      });
    }

    post.title = title || post.title;
    post.content = content || post.content;
    post.category = category || post.category;
    post.tags = tags || post.tags;
    post.metadata.updatedAt = new Date();

    await post.save();

    res.json({
      success: true,
      message: 'Post updated successfully',
      data: { post }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error updating post'
    });
  }
};

// DELETE /api/forum/posts/:postId
export const deletePost = async (req, res) => {
  try {
    const { postId } = req.params;
    const userId = req.user.id;
    const userAnonymousId = generateAnonymousId(userId);

    const post = await Post.findById(postId);
    if (!post) {
      return res.status(404).json({
        success: false,
        message: 'Post not found'
      });
    }

    // Check ownership
    if (post.anonymousId !== userAnonymousId) {
      return res.status(403).json({
        success: false,
        message: 'Not authorized to delete this post'
      });
    }

    post.metadata.isActive = false;
    await post.save();

    res.json({
      success: true,
      message: 'Post deleted successfully'
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error deleting post'
    });
  }
};

// GET /api/forum/my-posts
export const getMyPosts = async (req, res) => {
  try {
    const userId = req.user.id;
    const userAnonymousId = generateAnonymousId(userId);
    const { page = 1, limit = 10 } = req.query;
    const skip = (page - 1) * limit;

    const posts = await Post.find({
      anonymousId: userAnonymousId,
      'metadata.isActive': true
    })
    .sort({ 'metadata.createdAt': -1 })
    .skip(parseInt(skip))
    .limit(parseInt(limit))
    .populate('instituteId', 'name');

    const total = await Post.countDocuments({
      anonymousId: userAnonymousId,
      'metadata.isActive': true
    });

    const postsWithOwnership = posts.map(post => ({
      ...post.toObject(),
      isMyPost: true,
      canEdit: true,
      canDelete: true
    }));

    res.json({
      success: true,
      data: {
        posts: postsWithOwnership,
        pagination: {
          current: parseInt(page),
          pages: Math.ceil(total / limit),
          total
        }
      }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error fetching your posts'
    });
  }
};




// export const getTopTaggedUpvotes = async (req, res) => {
//   try {
//     // Step 1: Find post with max upvotes and one tag
//     const topPost = await Post.findOne({ 'metadata.isActive': true })
//       .sort({ 'engagement.upvotes': -1 })
//       .select('title tags engagement.upvotes')
//       .lean();

//     const topTag = topPost?.tags?.[0] || null;

//     // Step 2: Aggregate tags with their total upvotes
//     const tagAggregation = await Post.aggregate([
//       { $match: { 'metadata.isActive': true } },
//       { $unwind: '$tags' },
//       {
//         $group: {
//           _id: '$tags',
//           totalUpvotes: { $sum: '$engagement.upvotes' }
//         }
//       },
//       { $sort: { totalUpvotes: -1 } }
//     ]);

//     const totalUpvotesAllTags = tagAggregation.reduce((acc, tag) => acc + tag.totalUpvotes, 0);

//     // Calculate percentage for top 5 tags
//     const topTagsWithPercent = tagAggregation.slice(0, 5).map(tag => ({
//       tag: tag._id,
//       totalUpvotes: tag.totalUpvotes,
//       percent: totalUpvotesAllTags ? ((tag.totalUpvotes / totalUpvotesAllTags) * 100).toFixed(2) : '0.00'
//     }));

//     res.json({
//       success: true,
//       data: {
//         topPost: {
//           title: topPost.title,
//           topTag,
//           upvotes: topPost.engagement.upvotes
//         },
//         trendingTags: topTagsWithPercent
//       }
//     });
//   } catch (error) {
//     console.error('Error getting top tagged upvotes:', error);
//     res.status(500).json({
//       success: false,
//       message: 'Error fetching trending tags and top post upvotes'
//     });
//   }
// };




export const getTrendingTagsByMaxUpvotes = async (req, res) => {
  try {
    // Find the maximum upvotes of any active post
    const maxUpvotePost = await Post.findOne({ 'metadata.isActive': true })
      .sort({ 'engagement.upvotes': -1 })
      .select('engagement.upvotes')
      .lean();

    if (!maxUpvotePost) {
      return res.json({ success: true, data: { trendingTags: [] } });
    }

    const maxUpvotes = maxUpvotePost.engagement.upvotes;

    // Aggregate tags from posts with max upvotes
    const tagCounts = await Post.aggregate([
      { $match: { 'metadata.isActive': true, 'engagement.upvotes': maxUpvotes } },
      { $unwind: '$tags' },
      {
        $group: {
          _id: '$tags',
          count: { $sum: 1 }
        }
      },
      { $sort: { count: -1 } },
      { $limit: 5 }
    ]);

    // Calculate total tags count for percentage calculation
    const totalTagCount = tagCounts.reduce((acc, tag) => acc + tag.count, 0);

    // Format tags with percentage
    const trendingTags = tagCounts.map(tag => ({
      tag: tag._id,
      count: tag.count,
      percent: totalTagCount ? ((tag.count / totalTagCount) * 100).toFixed(2) : '0.00'
    }));

    res.json({
      success: true,
      data: { trendingTags }
    });
  } catch (error) {
    console.error("Error fetching trending tags by max upvotes:", error);
    res.status(500).json({
      success: false,
      message: "Error fetching trending tags"
    });
  }
};
