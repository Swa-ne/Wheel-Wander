import express, {Express, Request, Response, Router} from "express"


import { authenticateToken } from "../controller/auth"
import { sendMessage } from "../controller/message"

const router = Router()

router.post("/send", authenticateToken, sendMessage)

module.exports = router