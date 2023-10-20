import express, {Express, Request, Response, Router} from "express"
import { uploadVehicleImage, uploadVehicleInformation } from "../controller/market"

import multer from 'multer'
import fileUpload from 'express-fileupload'
import path from 'path'
import { authenticateToken } from "../controller/auth"


const storage = multer.diskStorage({
    destination: (req, res, cb) => {
        cb(null, './Images')
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + path.extname(file.originalname))
    }
})
const upload = multer({storage: storage})

const router = Router()

// router.get("/getUsers/:userID", getUsers)
// router.post("/send", authenticateToken, sendMessage)
router.post("/uploadImage", upload.array("image"), uploadVehicleImage)
router.post("/uploadInformation", authenticateToken, uploadVehicleInformation)

module.exports = router