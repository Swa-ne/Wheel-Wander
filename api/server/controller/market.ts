import express, {Express, Request, Response} from "express"
import { HttpResponse } from "../models/http-response"
import { getBestVehiclesFromDatabase, getVehiclesDetailsFromDatabase, getVehiclesFromDatabase, getVehiclesTypeFromDatabase, uploadVehicleDetails, uploadVehicleImages } from "../models/market"

export const uploadVehicleImage = async (req : Request, res : Response) => {
    try {
        await uploadVehicleImages(req.files, req.body.plateNumber, req.body.type)
        res.status(200).json({"message": "success"})
        return;
    } catch {
        res.status(500).json({"message": "Internal Server Error"})
        return;
    }
}

export const uploadVehicleInformation = async (req : Request, res : Response) => {
    try {
        await uploadVehicleDetails(req.body.userID, req.body)
        res.status(200).json({"message": "success"})
        return;
    } catch {
        res.status(500).json({"message": "Internal Server Error"})
        return;
    }
}

export const getVehicles = async (req : Request, res : Response) => {
    try {
        const result = await getVehiclesFromDatabase()
        res.status(200).send(result)
        return;
    } catch {
        res.status(500).json({"message": "Internal Server Error"})
        return;
    }
}

export const getVehiclesType = async (req : Request, res : Response) => {
    try {
        const result = await getVehiclesTypeFromDatabase(req.params.type)
        res.status(200).send(result)
        return;
    } catch {
        res.status(500).json({"message": "Internal Server Error"})
        return;
    }
}
export const getVehiclesID = async (req : Request, res : Response) => {
    // try {
        const result = await getVehiclesDetailsFromDatabase(req.params.id)
        res.status(200).send(result)
        return;
    // } catch {
    //     res.status(500).json({"message": "Internal Server Error"})
    //     return;
    // }
}

export const getBestVehicles = async (req : Request, res : Response) => {
    try {
        const result = await getBestVehiclesFromDatabase()
        res.status(200).send(result)
        return;
    } catch {
        res.status(500).json({"message": "Internal Server Error"})
        return;
    }
}