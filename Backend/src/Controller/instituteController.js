// controllers/instituteController.js
import Institute from '../Models/instituteModel.js';
import jwt from 'jsonwebtoken';

// Generate JWT Token for Institute
const generateToken = (instituteId) => {
  return jwt.sign({ id: instituteId, type: 'institute' }, process.env.JWT_SECRET, { 
    expiresIn: process.env.JWT_EXPIRE || '7d' 
  });
};

// Institute SignUp
export const signUp = async (req, res) => {
  try {
    const {
      name,
      address,
      contactInfo
    } = req.body;

    // Check if institute already exists
    const existingInstitute = await Institute.findOne({ 
      $or: [
        { 'contactInfo.email': contactInfo.email },
        { name: name }
      ]
    });
    
    if (existingInstitute) {
      return res.status(400).json({
        success: false,
        message: 'Institute with this name or email already exists'
      });
    }

    // Create new institute
    const newInstitute = new Institute({
      name,
      address,
      contactInfo
    });

    await newInstitute.save();

    // Generate token
    const token = generateToken(newInstitute._id);

    res.status(201).json({
      success: true,
      message: 'Institute registered successfully',
      token,
      institute: {
        id: newInstitute._id,
        name: newInstitute.name,
        address: newInstitute.address,
        contactInfo: newInstitute.contactInfo
      }
    });

  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Institute registration failed',
      error: error.message
    });
  }
};

// Institute SignIn
export const signIn = async (req, res) => {
  try {
    const { email, instituteCode } = req.body; // Using email and instituteCode for login

    // Validate input
    if (!email || !instituteCode) {
      return res.status(400).json({
        success: false,
        message: 'Please provide email and institute code'
      });
    }

    // Find institute by email
    const institute = await Institute.findOne({ 
      'contactInfo.email': email 
    });
    
    if (!institute) {
      return res.status(401).json({
        success: false,
        message: 'Invalid credentials'
      });
    }

    // You can add institute code verification here if needed
    // For now, we'll just verify the email exists

    // Generate token
    const token = generateToken(institute._id);

    res.json({
      success: true,
      message: 'Institute login successful',
      token,
      institute: {
        id: institute._id,
        name: institute.name,
        address: institute.address,
        contactInfo: institute.contactInfo
      }
    });

  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Institute login failed',
      error: error.message
    });
  }
};
