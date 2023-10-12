import express, {Express, Request, Response} from "express"
import { HttpResponse } from "../models/http-response"

 
export const sendMessage = async (req : Request, res : Response) => {
    try {
        const message : String = req.body.message;
        res.status(200).json({"message": "valid"})
        return;
    } catch {
        res.status(500).json({"message": "Internal Server Error"})
        return;
    }
}