import express from 'express';
import { registerInstitute } from '../Controller/instituteController.js';

const instituteRouter = express.Router();

instituteRouter.post('/register', registerInstitute);

export default instituteRouter;
