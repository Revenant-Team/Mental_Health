import express from 'express';
import { signUp,signIn } from '../Controller/instituteController.js';


const instituteRouter = express.Router();

instituteRouter.post('/signup', signUp);
instituteRouter.post('/signin', signIn);

export default instituteRouter;
