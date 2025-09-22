import ForumPost from "../Models/postModel.js";

// Create Forum Post
export const createPost = async (req, res) => {
  try {
    const { content, authorId, instituteId } = req.body;

    let postAuthorId = null;
    let postInstituteId = null;
    

    if (req.user) {
      postAuthorId = req.user._id;
      postInstituteId = req.user.profile?.instituteID || null;
    }
   
    if (authorId) {
      postAuthorId = authorId;
    }
    if (instituteId) {
      postInstituteId = instituteId;
    }

   
    if (!content || !content.title || !content.body || !content.category) {
      return res.status(400).json({
        success: false,
        message: 'Title, body, and category are required'
      });
    }


    const newPost = new ForumPost({
      authorId: postAuthorId, // Can be null
      instituteId: postInstituteId, // Can be null
      content: {
        title: content.title,
        body: content.body,
        category: content.category,
        tags: content.tags || []
      },
      likes: 0
    });

    await newPost.save();

    const populatedPost = await ForumPost.findById(newPost._id)
      .populate('authorId', 'profile.firstname profile.lastname')
      .populate('instituteId', 'name');

    res.status(201).json({
      success: true,
      message: 'Forum post created successfully',
      data: populatedPost
    });

  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Failed to create post',
      error: error.message
    });
  }
};

// Fetch Forum Posts
export const fetchPosts = async (req, res) => {
  try {
    const { 
      page = 1, 
      limit = 10, 
      category, 
      instituteId 
    } = req.query;

    const filter = {};
    
    if (category) {
      filter['content.category'] = category;
    }
    
    if (instituteId) {
      filter.instituteId = instituteId;
    }

 
    const skip = (page - 1) * limit;

   
    const posts = await ForumPost.find(filter)
      .populate('authorId', 'profile.firstname profile.lastname')
      .populate('instituteId', 'name')
      .sort({ createdAt: -1 }) // Latest posts first
      .skip(skip)
      .limit(parseInt(limit));

  
    const totalPosts = await ForumPost.countDocuments(filter);
    const totalPages = Math.ceil(totalPosts / limit);

    res.json({
      success: true,
      data: {
        posts,
        pagination: {
          currentPage: parseInt(page),
          totalPages,
          totalPosts,
          hasNextPage: page < totalPages,
          hasPrevPage: page > 1
        }
      }
    });

  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Failed to fetch posts',
      error: error.message
    });
  }
};

// Get Single Post
export const getPost = async (req, res) => {
  try {
    const { id } = req.params;

    const post = await ForumPost.findById(id)
      .populate('authorId', 'profile.firstname profile.lastname')
      .populate('instituteId', 'name');

    if (!post) {
      return res.status(404).json({
        success: false,
        message: 'Post not found'
      });
    }

    res.json({
      success: true,
      data: post
    });

  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Failed to fetch post',
      error: error.message
    });
  }
};
