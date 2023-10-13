import express, {Express, Request, Response} from "express"
import { HttpResponse } from "../models/http-response"
import { createConversation, getPastMessages, getChatID, addMessagetoDatabase, getUserID, getAllChat } from "../models/message";

export const getUsers = async (req : Request, res : Response) => {
    try {
        const userID = req.params.userID
        const users = getAllChat(userID)
        res.status(200).json(users)
    } catch {
        res.status(500).json({"message": "Internal Server Error"})
        return;
    }
}
export const openConversation = async (req : Request, res : Response) => {
    try {
        const { sender, receiver } : any = req.body;
        const chatID = await getChatID(sender, receiver)
        if(chatID.length > 0){
            let messages : any = await getPastMessages(chatID)
            if(messages.length == 0) {
                messages = {"message" : "NO MESSAGES"}
            }
            res.status(200).json(messages)
        } else {
            const user1ID : any = await getUserID(sender)
            const user2ID : any = await getUserID(receiver)
            if (await createConversation(user1ID, user2ID)) {
                res.status(200).json({"message": "success"})
            } else {    
                res.status(500).json({"message": "Internal Server Error"})
            }
            return;
        }
        return;
    } catch {
        res.status(500).json({"message": "Internal Server Error"})
        return;
    }
}

export const sendMessage = async (req : Request, res : Response) => {
    try {
        const { sender, receiver, message } : any = req.body;
        const chatID = await getChatID(sender, receiver)

        if(chatID){
            addMessagetoDatabase(chatID, message, sender)
        } else {
            if (await createConversation(sender, receiver)) {
                res.status(200).json({"message": "success"})
            } else {    
                res.status(500).json({"message": "Internal Server Error"})
            }
            return;
        }
    } catch {
        res.status(500).json({"message": "Internal Server Error"})
        return;
    }
}