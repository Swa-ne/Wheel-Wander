import mysql from "mysql2"
import { HttpResponse } from "./http-response"

const bcrypt = require("bcrypt")

// Connecting to the Database
const pool = mysql.createPool({
    host: process.env.MYSQL_HOST,
    user: process.env.MYSQL_USER,
    password: process.env.MYSQL_PASSWORD,
    database: process.env.MYSQL_DATABASE
}).promise()

export const loginUsertoDatabase = async (userIdentifier: String, password : String, userIdentifierType : String) => {
    try {
        let [result] : any = await pool.query(`SELECT PasswordHash FROM user_login_data WHERE ${userIdentifierType} = ?;`, [userIdentifier])
        if(result.length > 0){
            result = result[0]["PasswordHash"]
            if(await bcrypt.compare(password, result)){
                return new HttpResponse({"message" : "success"}, 200);
            }
            return new HttpResponse({"message" : "Wrong Password."}, 200);
        }
        return new HttpResponse({"message" : "User not Found."}, 200);
    } catch {
        return new HttpResponse({"message" : "Internal Server Error."}, 500);
    }
}
export const registerUsertoDatabase = async (username : String, emailAddress: String, password : String) => {
    try {
        const saltRounds = await bcrypt.genSalt();
        password = await bcrypt.hash(password, saltRounds)
        const [result] = await pool.query(`INSERT INTO user_login_data (username, emailAddress, PasswordHash, DateCreated) VALUES (?, ?, ?, curdate());`, [username, emailAddress, password])
        return true
    } catch {
        return false
    }
}

export const checkUsernameAvailability = async (username : String) : Promise<boolean> => {
    const [result] : Array<any> = await pool.query(`SELECT * FROM user_login_data WHERE Username = ?;`, username)
    
    return result.length == 0
}

export const checkEmailAvailability = async (emailAddress : String) : Promise<boolean> => {
    const [result] : Array<any> = await pool.query(`SELECT * FROM user_login_data WHERE emailAddress = ?;`, emailAddress)
    
    return result.length == 0
}

// export const addRefreshToken = async (refreshToken : String) => {
//     try{
//         const [result] : Array<any> = await pool.query(`INSERT INTO refresh_token (token) VALUES (?)`, refreshToken)
//         return true
//     } catch {
//         return false
//     }

// }
// export const checkRefreshToken = async (refreshToken : String) => {
//     const [result] : Array<any> = await pool.query(`SELECT * FROM refresh_token WHERE token = ?;`, refreshToken)
    
//     return result.length == 0
// }
// export const deleteRefreshToken = async (refreshToken : String) => {
//     // TODO: Delete token from db
//     try{
//         const [result] : Array<any> = await pool.query(`INSERT INTO refresh_token (token) VALUES (?)`, refreshToken)
//         return true
//     } catch {
//         return false
//     }
// }