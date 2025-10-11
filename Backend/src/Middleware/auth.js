import jwt from 'jsonwebtoken';
import User from '../Models/userModel.js';

export const authMiddleware = async (req, res, next) => {
  try {
    // Get token directly from 'token' header
    const token = req.headers.token;
    
    if (!token) {
      return res.status(401).json({
        success: false,
        message: 'Access denied. No token provided.'
      });
    }

    const decoded = jwt.verify(token, process.env.JWT_SECRET);               
    const user = await User.findById(decoded.id).select('-hashedPassword');
    
    if (!user || !user.isActive) {
      return res.status(401).json({
        success: false,
        message: 'Invalid token or user not active.'
      });
    }

    req.user = user;
    next();
  } catch (error) {
    res.status(401).json({
      success: false,
      message: 'Invalid token.'
    });
  }
};
