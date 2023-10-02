import { error } from "console"
import mysql from "mysql2"

const bcrypt = require("bcrypt")

// Connecting to the Database
const pool = mysql.createPool({
    host: process.env.MYSQL_HOST,
    user: process.env.MYSQL_USER,
    password: process.env.MYSQL_PASSWORD,
    database: process.env.MYSQL_DATABASE
}).promise()

export const registerUsertoDatabase = async (username : String, emailAddress: String, password : String) => {
    const saltRounds = Math.floor(Math.random() * 20) + 1;
    password = await bcrypt.hash(password, saltRounds)
    const [result] = await pool.query(`INSERT INTO user_login_data (username, emailAddress, PasswordHash, DateCreated, SaltRounds) VALUES (?, ?, ?, curdate(), ?);`, [username, emailAddress, password, saltRounds])
    return result
}

export const checkUsernameAvailability = async (username : String) : Promise<boolean> => {
    const [result] : Array<any> = await pool.query(`SELECT * FROM user_login_data WHERE Username = ?;`, username)
    
    return result.length == 0
}

export const checkEmailAvailability = async (emailAddress : String) : Promise<boolean> => {
    const [result] : Array<any> = await pool.query(`SELECT * FROM user_login_data WHERE emailAddress = ?;`, emailAddress)
    
    return result.length == 0
}

export const checkUsernameValidity = (username : String) => {
    // TODO: max 25 characters
    const regex = /^[a-zA-Z0-9]+$/;
    
    return username.match(regex)
}

export const checkEmailValidity = (emailAddress : String) => {
    const regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/;
    
    return emailAddress.match(regex)
}

export const checkPasswordValidity = (password : String) => {
    const regex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s)./;
    // least one lowercase letter, one uppercase letter, one numeric digit, and one special character
    return password.match(regex)
}
