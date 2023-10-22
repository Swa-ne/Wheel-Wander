import express, {Express, Request, Response, Router} from "express"
import { getBestVehicles, getVehicles, getVehiclesID, getVehiclesType, uploadVehicleImage, uploadVehicleInformation } from "../controller/market"

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

router.get("/getVehicles", getVehicles)
router.get("/getVehicle/:id", getVehiclesID)
router.get("/getVehicles/:type", getVehiclesType)
router.get("/getBestVehicles", getBestVehicles)
router.post("/uploadImage", upload.array("image"), uploadVehicleImage)
router.post("/uploadInformation", authenticateToken, uploadVehicleInformation)

module.exports = router