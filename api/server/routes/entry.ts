import express, {Express, Request, Response, Router} from "express"


import { registerUserController } from "../controller/entry"

const router = Router()

router.post("/signup", registerUserController)

module.exports = router