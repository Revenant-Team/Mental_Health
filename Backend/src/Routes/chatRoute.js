import express, { Router } from 'express'
import { chatting } from '../Controller/ChatController.js'

const chatRouter = Router()

chatRouter.post('/chat_with_bot',chatting)

export default chatRouter