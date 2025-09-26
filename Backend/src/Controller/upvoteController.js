import Post from '../Models/postModel.js';
import Reply from '../Models/replyModel.js';
import Upvote from '../Models/upvoteModel.js';
import { generateAnonymousId } from '../utlis/anonymousId.js';

// POST /api/forum/posts/:postId/upvote
export const togglePostUpvote = async (req, res) => {
  try {
    const { postId } = req.params;
    const userId = req.user.id;
    const anonymousId = generateAnonymousId(userId);

    const post = await Post.findById(postId);
    if (!post) {
      return res.status(404).json({
        success: false,
        message: 'Post not found'
      });
    }

    // Check if user already upvoted
    const existingUpvote = await Upvote.findOne({
      anonymousId,
      targetId: postId,
      targetType: 'post'
    });

    if (existingUpvote) {
      // Remove upvote
      await Upvote.deleteOne({ _id: existingUpvote._id });
      await Post.findByIdAndUpdate(postId, {
        $inc: { 'engagement.upvotes': -1 },
        $pull: { upvotedBy: anonymousId }
      });

      res.json({
        success: true,
        message: 'Upvote removed',
        data: { upvoted: false }
      });
    } else {
      // Add upvote
      await new Upvote({
        anonymousId,
        targetId: postId,
        targetType: 'post'
      }).save();

      await Post.findByIdAndUpdate(postId, {
        $inc: { 'engagement.upvotes': 1 },
        $addToSet: { upvotedBy: anonymousId }
      });

      res.json({
        success: true,
        message: 'Post upvoted',
        data: { upvoted: true }
      });
    }
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error toggling upvote'
    });
  }
};

// POST /api/forum/replies/:replyId/upvote
export const toggleReplyUpvote = async (req, res) => {
  try {
    const { replyId } = req.params;
    const userId = req.user.id;
    const anonymousId = generateAnonymousId(userId);

    const reply = await Reply.findById(replyId);
    if (!reply) {
      return res.status(404).json({
        success: false,
        message: 'Reply not found'
      });
    }

    // Check if user already upvoted
    const existingUpvote = await Upvote.findOne({
      anonymousId,
      targetId: replyId,
      targetType: 'reply'
    });

    if (existingUpvote) {
      // Remove upvote
      await Upvote.deleteOne({ _id: existingUpvote._id });
      await Reply.findByIdAndUpdate(replyId, {
        $inc: { 'engagement.upvotes': -1 },
        $pull: { upvotedBy: anonymousId }
      });

      res.json({
        success: true,
        message: 'Upvote removed',
        data: { upvoted: false }
      });
    } else {
      // Add upvote
      await new Upvote({
        anonymousId,
        targetId: replyId,
        targetType: 'reply'
      }).save();

      await Reply.findByIdAndUpdate(replyId, {
        $inc: { 'engagement.upvotes': 1 },
        $addToSet: { upvotedBy: anonymousId }
      });

      res.json({
        success: true,
        message: 'Reply upvoted',
        data: { upvoted: true }
      });
    }
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error toggling upvote'
    });
  }
};

// GET /api/forum/posts/:postId/upvotes
export const getPostUpvotes = async (req, res) => {
  try {
    const { postId } = req.params;

    const post = await Post.findById(postId).select('engagement.upvotes');
    if (!post) {
      return res.status(404).json({
        success: false,
        message: 'Post not found'
      });
    }

    res.json({
      success: true,
      data: { upvotes: post.engagement.upvotes }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error fetching upvotes'
    });
  }
};

// GET /api/forum/replies/:replyId/upvotes
export const getReplyUpvotes = async (req, res) => {
  try {
    const { replyId } = req.params;

    const reply = await Reply.findById(replyId).select('engagement.upvotes');
    if (!reply) {
      return res.status(404).json({
        success: false,
        message: 'Reply not found'
      });
    }

    res.json({
      success: true,
      data: { upvotes: reply.engagement.upvotes }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error fetching upvotes'
    });
  }
};
