import express, {Express, Request, Response, Router} from "express"


import { authenticateToken } from "../controller/auth"
import { getUsers, openConversation, sendMessage } from "../controller/message"

const router = Router()

router.get("/getUsers/:userID", getUsers)
router.post("/send", authenticateToken, sendMessage)
router.post("/open", openConversation)

module.exports = router