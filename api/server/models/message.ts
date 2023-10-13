import { pool } from "./database-connection"
import { HttpResponse } from "./http-response"




export const getAllChat = async (userID : String) => {
    try {
        const [result] : Array<any> = await pool.query(`SELECT * FROM conversation WHERE (User1ID = ? OR User2ID = ?);`, [userID, userID])
        return result
    } catch {
        return false
    }
}

export const getChatID = async (user1ID : String, user2ID : String) => {
    try {
        const [result] : Array<any> = await pool.query(`SELECT ChatID FROM conversation WHERE (User1ID = ? OR User1ID = ?) AND (User2ID = ? OR User2ID = ?);`, [user1ID, user2ID, user1ID, user2ID])
        return result
    } catch {
        return false
    }
}

export const createConversation = async (user1ID : String, user2ID : String) => {
    try {
        const [result] = await pool.query(`INSERT INTO conversation (User1ID, User2ID) VALUES (?, ?);`, [user1ID, user2ID])
        return true
    } catch {
        return false
    }
}

export const addMessagetoDatabase = async (chatID : any, message: String, sender : any) => {
    try {
        chatID = chatID[0]["ChatID"]
        sender = getUserID(sender)
        const [result] = await pool.query(`INSERT INTO message (chatID, Message, Sender, DateSent) VALUES (?, ?, ?, now());`, [chatID, message, sender])
        return true
    } catch {
        return false
    }
}

export const getPastMessages = async (chatID : String) : Promise<boolean> => {
    const [result] : Array<any> = await pool.query(`SELECT * FROM message WHERE ChatID = ?;`, chatID)
    
    return result
}

export const getUserID = async (user : String) : Promise<boolean> => {
    const [result] : Array<any> = await pool.query(`SELECT UserID FROM user_login_data WHERE Username = ?;`, user)
    
    return result
}
