import express, {Express, Request, Response} from "express"
import { checkEmailAvailability, checkUsernameAvailability, loginUsertoDatabase, registerUsertoDatabase } from "../models/entry";
import { HttpResponse } from "../models/http-response"

// Logins
export const loginUserController = async (req : Request, res : Response) => {
    const userIdentifier : String = req.body.userIdentifier;
    const password : String = req.body.password;
    
    const userIdentifierType = await checkInputType(userIdentifier)
    const checkerForInput = await checkEveryInputForLogin(userIdentifier, password, userIdentifierType)
    if(checkerForInput.code !== 409){
        const data = await loginUsertoDatabase(userIdentifier, password, userIdentifierType)
        res.status(data.code).json(data.message)
        return;
    }
    
    res.status(checkerForInput.code).json(checkerForInput.message)
    return;
}

const checkInputType = async (userIdentifier : String) => {
    
    return (userIdentifier.includes("@")) ? "EmailAddress" : "Username";
}

const checkEveryInputForLogin = async (userIdentifier : String, password : String, userIdentifierType : String) => {
    if(userIdentifierType === "Username"){
        if (!checkUsernameValidity(userIdentifier)) {
            return new HttpResponse({"message": "Invalid Username"}, 409);
        }
    } else {
        if (!checkEmailValidity(userIdentifier)) {
            return new HttpResponse({"message": "Invalid Email."}, 409);
        }
    }
    if (!checkPasswordValidity(password)) {
        return new HttpResponse({"message": "Invalid Password."}, 409);
    }
    return new HttpResponse({"message":"success"}, 200);
}

// Registrations
export const registerUserController = async (req : Request, res : Response) => {
    const username : String = req.body.username;
    const emailAddress : String = (req.body.emailAddress).toLowerCase();
    const comfirmationEmailAddress : String = (req.body.comfirmationEmailAddress).toLowerCase();
    const password : String = req.body.password;
    const comfirmationPassword : String = req.body.comfirmationPassword;
    
    const checkerForInput = await checkEveryInputForSignup(username, emailAddress, comfirmationEmailAddress, password, comfirmationPassword)
    if(checkerForInput.code !== 409){
        const data = registerUsertoDatabase(username, emailAddress, password)
        if (!data) {
            res.status(500).json({"message": "Internal Server Error"})
            return;
        }
    }
    
    res.status(checkerForInput.code).json(checkerForInput.message)
    return;
}

const checkEveryInputForSignup = async (username : String, emailAddress : String, comfirmationEmailAddress : String, password : String, comfirmationPassword : String) : Promise<HttpResponse> => {
    if (!checkUsernameValidity(username)) {
        return new HttpResponse({"message":"Username must only contains letters and numbers."}, 409);
    }
    if (!checkEmailValidity(emailAddress)) {
        return new HttpResponse({"message":"Invalid Email."}, 409);
    }
    if (!checkPasswordValidity(password)) {
        return new HttpResponse({"message":"Password must have at least one lowercase letter, one uppercase letter, one numeric digit, and one special character."}, 409);
    }
    if (!await checkUsernameAvailability(username)) {
        return new HttpResponse({"message":"This username is being used."}, 409);
    }
    if (!await checkEmailAvailability(emailAddress)) {
        return new HttpResponse({"message":"This email address is being used."}, 409);
    }
    if (emailAddress !== comfirmationEmailAddress) {
        return new HttpResponse({"message":"Those email address didn't match. Try again."}, 409);
    }
    if (password !== comfirmationPassword) {
        return new HttpResponse({"message":"Those password didn't match. Try again."}, 409);
    }
    return new HttpResponse({"message":"success"}, 200);
}

const checkUsernameValidity = (username : String) => {
    // TODO: max 25 characters
    const regex = /^[a-zA-Z0-9]+$/;
    
    return username.match(regex)
}

const checkEmailValidity = (emailAddress : String) => {
    const regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/;
    
    return emailAddress.match(regex)
}

const checkPasswordValidity = (password : String) => {
    const regex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s)./;
    // least one lowercase letter, one uppercase letter, one numeric digit, and one special character
    return password.match(regex)
}