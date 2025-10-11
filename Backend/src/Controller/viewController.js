import Post from "../Models/postModel.js";


export const incrementPostView = async (req, res) => {
  try {
    const { postId } = req.params;

    const post = await Post.findByIdAndUpdate(
      postId,
      { $inc: { 'engagement.views': 1 } },
      { new: true }
    );

    if (!post) {
      return res.status(404).json({
        success: false,
        message: 'Post not found'
      });
    }

    res.json({
      success: true,
      data: { views: post.engagement.views }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error incrementing view count'
    });
  }
};


