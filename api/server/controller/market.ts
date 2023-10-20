import express, {Express, Request, Response} from "express"
import { HttpResponse } from "../models/http-response"

export const uploadVehicleImage = async (req : Request, res : Response) => {
    try {
        console.log(req.files)
        res.status(200).json({"message": "success"})
        return;
    } catch {
        res.status(500).json({"message": "Internal Server Error"})
        return;
    }
}

export const uploadVehicleInformation = async (req : Request, res : Response) => {
    try {
        console.log(req.body)
        // const { senderID, receiverID, message } : any = req.body;
        // let chatID = await getChatID(senderID, receiverID)
        // if(chatID.length > 0){
        //     addMessagetoDatabase(chatID, message, senderID)
        // } else {
        //     let success =  await createConversation(senderID, receiverID)
        //     if (success[0]) {
        //         addMessagetoDatabase(success[1], message, senderID)
                res.status(200).json({"message": "success"})
        //     } else {    
        //         res.status(500).json({"message": "Internal Server Error"})
        //     }
        //     return;
        // }
        
        return;
    } catch {
        res.status(500).json({"message": "Internal Server Error"})
        return;
    }
}