import express, {Express, Request, Response, Router} from "express"


import { loginUserController, registerUserController } from "../controller/entry"

const router = Router()

router.post("/signup", registerUserController)
router.post("/login", loginUserController)

module.exports = router