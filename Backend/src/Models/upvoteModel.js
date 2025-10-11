import mongoose from 'mongoose';

const upvoteSchema = new mongoose.Schema({
  anonymousId: {
    type: String,
    required: true,
    index: true
  },
  targetType: {
    type: String,
    enum: ['post', 'reply'],
    required: true
  },
  targetId: {
    type: mongoose.Schema.Types.ObjectId,
    required: true,
    index: true
  },
  createdAt: {
    type: Date,
    default: Date.now
  }
}, {
  timestamps: true
});

// Compound index to prevent duplicate votes
upvoteSchema.index({ anonymousId: 1, targetId: 1, targetType: 1 }, { unique: true });

export default mongoose.model('Upvote', upvoteSchema);
