import express from 'express';
import {incrementPostView} from '../Controller/viewController.js';

const viewRouter = express.Router();

// View tracking and statistics
viewRouter.post('/posts/:postId/view', incrementPostView); 


export default viewRouter;
