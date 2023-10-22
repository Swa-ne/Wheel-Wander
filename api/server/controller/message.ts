import express, {Express, Request, Response} from "express"
import { HttpResponse } from "../models/http-response"
import { createConversation, getPastMessages, getChatID, addMessagetoDatabase, getUserID, getAllChat, getLatestMessage } from "../models/message";

export const getUsers = async (req : Request, res : Response) => {
    try {
        const userID = req.params.userID
        const users : any = await getAllChat(userID)
        const chatUsers : any = []
        let data : any = {message: "NO INBOX"}
        await Promise.all(users.map(async (user : any) => {
            if(user["User1ID"] != userID) {
                data = await getLatestMessage(user["User1ID"], userID)
            } else {
                data = await getLatestMessage(user["User2ID"], userID)
            }
            chatUsers.push({userID: userID, ...data})
        }))
        res.status(200).json(data)
    } catch {
        res.status(500).json({"message": "Internal Server Error"})
        return;
    }
}
export const openConversation = async (req : Request, res : Response) => {
    try {
        const { senderID, receiverID } : any = req.body;
        if (senderID == undefined || receiverID == undefined) return res.status(500).json({"message": "Internal Server Error"})
        const chatID = await getChatID(senderID, receiverID)
    if(chatID[0]["ChatID"] > 0){
        let messages : any = await getPastMessages(chatID[0]["ChatID"])
            if(messages.length == 0) {
                messages = {"message" : "NO MESSAGES"}
            }
            res.status(200).json(messages)
        } else {
            if (await createConversation(senderID, receiverID)) {
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
        const { senderID, receiverID, message } : any = req.body;
        let chatID = await getChatID(senderID, receiverID)
        if(chatID.length > 0){
            addMessagetoDatabase(chatID, message, senderID)
        } else {
            let success =  await createConversation(senderID, receiverID)
            if (success[0]) {
                addMessagetoDatabase(success[1], message, senderID)
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