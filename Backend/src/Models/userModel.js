// models/User.js
import mongoose from 'mongoose';
import bcrypt from 'bcryptjs';

const UserSchema = new mongoose.Schema({
  email: { 
    type: String, 
    required: true, 
    unique: true,
    trim: true,
    lowercase: true
  },
  
  mobileno: { 
    type: String, 
    required: true,
    trim: true
  },
  
  password: { 
    type: String, 
    required: true,
    minLength: 6
  },
  
  role: {
    type: String,
    required: true,
    enum: ['student', 'counsellor', 'admin'],
    default: 'student'
  },
  
  profile: {
    firstname: { 
      type: String, 
      required: true,
      trim: true
    },
    lastname: { 
      type: String, 
      required: true,
      trim: true
    },
    dob: { 
      type: Date, 
      required: true
    },
    gender: {
      type: String,
      required: true,
      enum: ['male', 'female', 'other']
    },
    instituteID: { 
      type: mongoose.Schema.Types.ObjectId, 
      ref: 'Institute', 
      required: true 
    },
    department: { 
      type: String, 
      required: true,
      trim: true
    },
    year: { 
      type: Number, 
      required: true,
      min: 1,
      max: 6
    }
  },
  
  contactInfo: {
    phone: String,
    email: String,
    alternateContact: String
  }
  
}, { 
  timestamps: true 
});

// Hash password before saving
UserSchema.pre('save', async function(next) {
  if (!this.isModified('password')) return next();
  this.password = await bcrypt.hash(this.password, 12);
  next();
});

// Compare password method
UserSchema.methods.comparePassword = async function(password) {
  return await bcrypt.compare(password, this.password);
};

const User = mongoose.model('User', UserSchema);
export default User;
