// models/Institute.js
import mongoose from 'mongoose';

const InstituteSchema = new mongoose.Schema({
  name: { 
    type: String, 
    required: true,
    trim: true
  },
  
  address: {
    street: { 
      type: String, 
      required: true,
      trim: true
    },
    city: { 
      type: String, 
      required: true,
      trim: true
    },
    state: { 
      type: String, 
      required: true,
      trim: true
    },
    country: { 
      type: String, 
      required: true,
      trim: true
    },
    zipcode: { 
      type: String, 
      required: true,
      trim: true
    }
  },
  
  contactInfo: {
    phone: { 
      type: String, 
      required: true,
      trim: true
    },
    email: { 
      type: String, 
      required: true,
      trim: true,
      lowercase: true
    },
    website: String,
    fax: String,
    alternatePhone: String
  }
  
}, { 
  timestamps: true 
});

const Institute = mongoose.model('Institute', InstituteSchema);
export default Institute;
