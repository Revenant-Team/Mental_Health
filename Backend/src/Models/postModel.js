// models/ForumPost.js
import mongoose from 'mongoose';

const ForumPostSchema = new mongoose.Schema({
  // Author Information - Made Optional
  authorId: { 
    type: mongoose.Schema.Types.ObjectId, 
    ref: 'User', 
    required: false // Changed from required: true
  },
  
  // Institute Reference - Made Optional
  instituteId: { 
    type: mongoose.Schema.Types.ObjectId, 
    ref: 'Institute', 
    required: false // Changed from required: true
  },
  
  // Post Content
  content: {
    title: { 
      type: String, 
      required: true, 
      trim: true,
      maxLength: 200
    },
    body: { 
      type: String, 
      required: true, 
      trim: true,
      maxLength: 5000
    },
    category: {
      type: String,
      required: true,
      enum: [
        'academic_stress', 
        'anxiety', 
        'depression', 
        'relationships', 
        'family_issues', 
        'self_esteem', 
        'sleep_problems', 
        'social_anxiety', 
        'career_concerns', 
        'general_support'
      ]
    },
    tags: [{
      type: String,
      trim: true,
      lowercase: true,
      maxLength: 30
    }]
  },
  
  // Engagement
  likes: { 
    type: Number, 
    default: 0 
  }
  
}, { 
  timestamps: true 
});

const ForumPost = mongoose.model('ForumPost', ForumPostSchema);
export default ForumPost;
