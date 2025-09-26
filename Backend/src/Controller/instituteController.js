import Institute from '../Models/instituteModel.js';

// POST /api/institute/register
export const registerInstitute = async (req, res) => {
  try {
    const {
      name,
      code,
      type,
      email,
      phone,
      adminName,
      adminEmail,
      city,
      state
    } = req.body;

    // Check if institute with same code exists
    const existingInstitute = await Institute.findOne({ 
      code: code.toUpperCase() 
    });

    if (existingInstitute) {
      return res.status(400).json({
        success: false,
        message: 'Institute with this code already exists'
      });
    }

    const institute = new Institute({
      name,
      code: code.toUpperCase(),
      type,
      email,
      phone,
      adminName,
      adminEmail,
      city,
      state
    });

    await institute.save();

    res.status(201).json({
      success: true,
      message: 'Institute registered successfully',
      data: { institute }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Error registering institute',
      error: error.message
    });
  }
};
