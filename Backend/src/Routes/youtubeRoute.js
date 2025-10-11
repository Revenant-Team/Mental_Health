import express from "express";
import { search } from "../Controller/youtubeController.js";

const youtubeRouter = express();

youtubeRouter.post('/search',search);

export default youtubeRouter;