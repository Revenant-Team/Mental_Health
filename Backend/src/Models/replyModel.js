import mongoose from 'mongoose';

const replySchema = new mongoose.Schema({
  postId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Post',
    required: true,
    index: true
  },
  parentReplyId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Reply',
    default: null
  },
  anonymousId: {
    type: String,
    required: true,
    index: true
  },
  content: {
    type: String,
    required: true,
    maxlength: 1000,
    trim: true
  },
  metadata: {
    createdAt: {
      type: Date,
      default: Date.now
    },
    isActive: {
      type: Boolean,
      default: true
    },
    depth: {
      type: Number,
      default: 0,
      min: 0,
      max: 5 // Limit nesting depth
    }
  },
  engagement: {
    upvotes: {
      type: Number,
      default: 0
    },
    childReplies: {
      type: Number,
      default: 0
    }
  },
  upvotedBy: [{
    type: String // anonymousIds
  }]
}, {
  timestamps: true
});

// Indexes for performance
replySchema.index({ postId: 1, 'metadata.createdAt': 1 });
replySchema.index({ parentReplyId: 1, 'metadata.createdAt': 1 });
replySchema.index({ anonymousId: 1, 'metadata.createdAt': -1 });

export default mongoose.model('Reply', replySchema);
