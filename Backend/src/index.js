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
import userRouter from './src/Routes/userRoute.js';
import instituteRouter from './src/Routes/instituteRoute.js';
import postRouter from './src/Routes/postRoute.js';

app.use('/api/users', userRouter);
app.use('/api/institutes', instituteRouter);
app.use('/api/forum', postRouter);

app.listen(port,()=>{
    console.log(`App running on port ${port}`);
})