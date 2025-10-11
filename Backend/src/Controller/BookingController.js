// GET /api/bookings/available?counselorId=xxx
import Booking from "../Models/bookingModel.js";

export const getAvailableSlots = async (req, res) => {
  try {
    const counselorId  = req.user.id;
    if (!counselorId) return res.status(400).json({ message: 'counselorId required' });

    const today = new Date();
    const days = [...Array(4)].map((_, i) => {
      const d = new Date(today);
      d.setDate(d.getDate() + i);
      return d;
    });

    const allSlots = ["10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"];
    const available = [];

    for (let day of days) {
      const dateStr = day.toISOString().split('T')[0]; // YYYY-MM-DD

      const bookings = await Booking.find({ counselorId, date: dateStr });
      const bookedSlots = bookings.map(b => b.startTime);

      const slots = allSlots.filter(slot => !bookedSlots.includes(slot));
      available.push({ date: dateStr, slots });
    }

    return res.json({ success: true, data: available });
  } catch (err) {
    console.error(err);
    return res.status(500).json({ success: false, message: 'Server error' });
  }
};


// POST /api/bookings

export const bookSession = async (req, res) => {
  try {
    const  userId  = req.user.id;
    const { counselorId, date, startTime } = req.body;

    if (!counselorId || !date || !startTime)
      return res.status(400).json({ message: 'All fields required' });

    const [hour, minute] = startTime.split(':');
    const endHour = parseInt(hour) + 1;
    const endTime = `${endHour.toString().padStart(2, '0')}:${minute}`;

    // Check if slot is already booked
    const exists = await Booking.findOne({ counselorId, date, startTime });
    if (exists) return res.status(400).json({ message: 'Slot already booked' });

    const booking = await Booking.create({
      userId,
      counselorId,
      date,
      startTime,
      endTime,
    });

    return res.json({ success: true, data: booking });
  } catch (err) {
    console.error(err);
    return res.status(500).json({ success: false, message: 'Server error' });
  }
};



// GET /api/bookings/my

export const getMyBookings = async (req, res) => {
  try {
    const userId  = req.user.id;
    const bookings = await Booking.find({ userId }).populate('counselorId', 'name email');
    return res.json({ success: true, data: bookings });
  } catch (err) {
    console.error(err);
    return res.status(500).json({ success: false, message: 'Server error' });
  }
};


