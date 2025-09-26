import Reply from '../Models/replyModel.js';
import Post from '../Models/postModel.js';
import { generateAnonymousId } from '../utlis/anonymousId.js';

// GET /api/forum/posts/:postId/replies
export const getPostReplies = async (req, res) => {
  try {
    const { postId } = req.params;
    
    const replies = await Reply.aggregate([
      {
        $match: {
          postId: mongoose.Types.ObjectId(postId),
          'metadata.isActive': true
        }
      },
      {
        $sort: { 'metadata.createdAt': 1 }
      },
      {
        $addFields: {
          isMyReply: false // Will be updated in frontend based on anonymousId
        }
      }
    ]);

    // Build nested structure [web:115][web:118]
    const nestedReplies = buildReplyTree(replies);

    res.json({
      success: true,
      data: { replies: nestedReplies }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error fetching replies'
    });
  }
};

// POST /api/forum/posts/:postId/replies
export const replyToPost = async (req, res) => {
  try {
    const { postId } = req.params;
    const { content } = req.body;
    const userId = req.user.id;
    const anonymousId = generateAnonymousId(userId);

    // Check if post exists
    const post = await Post.findById(postId);
    if (!post) {
      return res.status(404).json({
        success: false,
        message: 'Post not found'
      });
    }

    const reply = new Reply({
      postId,
      parentReplyId: null,
      anonymousId,
      content,
      metadata: {
        depth: 0
      }
    });

    await reply.save();

    // Update post reply count
    await Post.findByIdAndUpdate(postId, {
      $inc: { 'engagement.totalReplies': 1 }
    });

    res.status(201).json({
      success: true,
      message: 'Reply added successfully',
      data: { reply }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error adding reply'
    });
  }
};

// POST /api/forum/replies/:replyId/replies
export const replyToReply = async (req, res) => {
  try {
    const { replyId } = req.params;
    const { content } = req.body;
    const userId = req.user.id;
    const anonymousId = generateAnonymousId(userId);

    // Check if parent reply exists
    const parentReply = await Reply.findById(replyId);
    if (!parentReply) {
      return res.status(404).json({
        success: false,
        message: 'Parent reply not found'
      });
    }

    const reply = new Reply({
      postId: parentReply.postId,
      parentReplyId: replyId,
      anonymousId,
      content,
      metadata: {
        depth: parentReply.metadata.depth + 1
      }
    });

    await reply.save();

    // Update parent reply count
    await Reply.findByIdAndUpdate(replyId, {
      $inc: { 'engagement.childReplies': 1 }
    });

    // Update post total reply count
    await Post.findByIdAndUpdate(parentReply.postId, {
      $inc: { 'engagement.totalReplies': 1 }
    });

    res.status(201).json({
      success: true,
      message: 'Reply added successfully',
      data: { reply }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error adding nested reply'
    });
  }
};

// PUT /api/forum/replies/:replyId
export const updateReply = async (req, res) => {
  try {
    const { replyId } = req.params;
    const { content } = req.body;
    const userId = req.user.id;
    const userAnonymousId = generateAnonymousId(userId);

    const reply = await Reply.findById(replyId);
    if (!reply) {
      return res.status(404).json({
        success: false,
        message: 'Reply not found'
      });
    }

    // Check ownership
    if (reply.anonymousId !== userAnonymousId) {
      return res.status(403).json({
        success: false,
        message: 'Not authorized to update this reply'
      });
    }

    reply.content = content;
    reply.updatedAt = new Date();
    await reply.save();

    res.json({
      success: true,
      message: 'Reply updated successfully',
      data: { reply }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error updating reply'
    });
  }
};

// DELETE /api/forum/replies/:replyId
export const deleteReply = async (req, res) => {
  try {
    const { replyId } = req.params;
    const userId = req.user.id;
    const userAnonymousId = generateAnonymousId(userId);

    const reply = await Reply.findById(replyId);
    if (!reply) {
      return res.status(404).json({
        success: false,
        message: 'Reply not found'
      });
    }

    // Check ownership
    if (reply.anonymousId !== userAnonymousId) {
      return res.status(403).json({
        success: false,
        message: 'Not authorized to delete this reply'
      });
    }

    reply.metadata.isActive = false;
    await reply.save();

    res.json({
      success: true,
      message: 'Reply deleted successfully'
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error deleting reply'
    });
  }
};

// GET /api/forum/replies/:replyId
export const getReplyById = async (req, res) => {
  try {
    const { replyId } = req.params;

    const reply = await Reply.findById(replyId);
    if (!reply || !reply.metadata.isActive) {
      return res.status(404).json({
        success: false,
        message: 'Reply not found'
      });
    }

    res.json({
      success: true,
      data: { reply }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error fetching reply'
    });
  }
};

// Helper function to build nested reply tree
const buildReplyTree = (replies) => {
  const replyMap = {};
  const rootReplies = [];

  // Create a map of replies
  replies.forEach(reply => {
    replyMap[reply._id] = { ...reply, children: [] };
  });

  // Build the tree structure
  replies.forEach(reply => {
    if (reply.parentReplyId) {
      if (replyMap[reply.parentReplyId]) {
        replyMap[reply.parentReplyId].children.push(replyMap[reply._id]);
      }
    } else {
      rootReplies.push(replyMap[reply._id]);
    }
  });

  return rootReplies;
};
