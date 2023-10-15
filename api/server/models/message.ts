import { pool } from "./database-connection"
import { HttpResponse } from "./http-response"



export const getLatestMessage = async (userID : string, myUserID : String) => {
    try {
        let data : any= {}
        let [result] : Array<any> = await pool.query(`SELECT Username FROM user_login_data WHERE UserID = ?;`, [userID])
        data["username"] = result[0]["Username"]
        let [chatID] : any = await pool.query(`SELECT ChatID FROM conversation WHERE (User1ID = ? OR User1ID = ?) AND (User2ID = ? OR User2ID = ?);`, ["0", myUserID, "0", myUserID])
        result = await pool.query(`SELECT Message, DateSent FROM message WHERE ChatID = ? LIMIT 1;`, [chatID[0]["ChatID"]])
        data["message"] = result[0][0]["Message"]
        data["time"] = result[0][0]["DateSent"]
        result = await pool.query(`SELECT avatarImage FROM user_account WHERE UserID = ?`, [userID])
        data["avatar"] = result[0]["avatarImage"]
        return data
    } catch {
        return false
    }
}

export const getAllChat = async (userID : String) => {
    try {
        const [result] : Array<any> = await pool.query(`SELECT User1ID, User2ID FROM conversation WHERE (User1ID = ? OR User2ID = ?);`, [userID, userID])
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
        return [true, result]
    } catch {
        return [false, "fail"]
    }
}

export const addMessagetoDatabase = async (chatID : any, message: String, sender : any) => {
    try {
        chatID = chatID[0]["ChatID"]
        console.log(chatID, message, sender)
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
