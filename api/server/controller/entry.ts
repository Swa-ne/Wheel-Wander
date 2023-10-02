import express, {Express, Request, Response} from "express"
import { checkEmailAvailability, checkEmailValidity, checkPasswordValidity, checkUsernameAvailability, checkUsernameValidity, registerUsertoDatabase } from "../models/entry";
import { HttpResponse } from "../models/http-response"

export const registerUserController = async (req : Request, res : Response) => {
    const username : String = req.body.username;
    const emailAddress : String = req.body.emailAddress;
    const comfirmationEmailAddress : String = req.body.comfirmationEmailAddress;
    const password : String = req.body.password;
    const comfirmationPassword : String = req.body.comfirmationPassword;
    
    const checkerForInput = await checkEveryInput(username, emailAddress, comfirmationEmailAddress, password, comfirmationPassword)
    if(checkerForInput.code === 409){
        res.status(checkerForInput.code).json(checkerForInput.errorMessage)
        return;
    }
    
    registerUsertoDatabase(username, emailAddress, password)
    res.status(checkerForInput.code).json(checkerForInput.errorMessage)
    return;
}

const checkEveryInput = async (username : String, emailAddress : String, comfirmationEmailAddress : String, password : String, comfirmationPassword : String) : Promise<HttpResponse> => {
    if (!checkUsernameValidity(username)) {
        return new HttpResponse({"errorMessage":"Username must only contains letters and numbers."}, 409);
    }
    if (!checkEmailValidity(emailAddress)) {
        return new HttpResponse({"errorMessage":"Invalid Email."}, 409);
    }
    if (!checkPasswordValidity(password)) {
        return new HttpResponse({"errorMessage":"Password must have at least one lowercase letter, one uppercase letter, one numeric digit, and one special character."}, 409);
    }
    if (!await checkUsernameAvailability(username)) {
        return new HttpResponse({"errorMessage":"This username is being used."}, 409);
    }
    if (!await checkEmailAvailability(emailAddress)) {
        return new HttpResponse({"errorMessage":"This email address is being used."}, 409);
    }
    if (emailAddress !== comfirmationEmailAddress) {
        return new HttpResponse({"errorMessage":"Those email address didn't match. Try again."}, 409);
    }
    if (password !== comfirmationPassword) {
        return new HttpResponse({"errorMessage":"Those password didn't match. Try again."}, 409);
    }
    return new HttpResponse({"message":"success"}, 200);
}
