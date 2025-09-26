import mongoose from 'mongoose';

const postSchema = new mongoose.Schema({
  anonymousId: {
    type: String,
    required: true,
    index: true
  },
  instituteId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Institute',
    required: true,
    index: true
  },
  title: {
    type: String,
    required: true,
    maxlength: 200,
    trim: true
  },
  content: {
    type: String,
    required: true,
    maxlength: 2000,
    trim: true
  },
  category: {
    type: String,
    enum: ['Academic', 'Personal', 'Social', 'Health', 'Career', 'Other'],
    required: true
  },
  tags: [{
    type: String,
    maxlength: 30
  }],
  metadata: {
    createdAt: {
      type: Date,
      default: Date.now
    },
    updatedAt: {
      type: Date,
      default: Date.now
    },
    isActive: {
      type: Boolean,
      default: true
    },
    priority: {
      type: String,
      enum: ['Low', 'Medium', 'High', 'Critical'],
      default: 'Medium'
    }
  },
  engagement: {
    upvotes: {
      type: Number,
      default: 0
    },
    totalReplies: {
      type: Number,
      default: 0
    },
    views: {
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
postSchema.index({ anonymousId: 1, 'metadata.createdAt': -1 });
postSchema.index({ instituteId: 1, 'metadata.createdAt': -1 });
postSchema.index({ category: 1, 'metadata.createdAt': -1 });

export default mongoose.model('Post', postSchema);
