import express from 'express';
import cors from 'cors';
import 'dotenv/config';
import connectDB from './src/DB/db.js';

const app = express();


app.use(cors());
app.use(express.json());

connectDB();

const port = process.env.PORT || 4000;

//api endpoints
// import userRouter from './src/Routes/userRoute.js';
// import instituteRouter from './src/Routes/instituteRoute.js';
// import postRouter from './src/Routes/postRoute.js';
import userRouter from './src/Routes/userRoute.js';
import instituteRouter from './src/Routes/instituteRoute.js';
import postRouter from './src/Routes/postRoute.js';
import replyRouter from './src/Routes/replyRoute.js';
import upvoteRouter from './src/Routes/upvoteRoute.js';
import viewRouter from './src/Routes/viewRoute.js';
import chatRouter from './src/Routes/chatRoute.js';

app.use('/api/users', userRouter);
app.use('/api/institutes', instituteRouter);
app.use('/api/forum', postRouter);
app.use('/api/reply',replyRouter);
app.use('/api/upvote',upvoteRouter);
app.use('/api/view',viewRouter);
app.use('/api/chat', chatRouter);

app.listen(port,()=>{
    console.log(`App running on port ${port}`);
})