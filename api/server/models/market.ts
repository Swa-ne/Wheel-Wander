import { pool } from "./database-connection"
import { HttpResponse } from "./http-response"


export const uploadVehicleDetails = async (userID : String, vehicleDetails : any) => {
    // try {
        let vehicleType : String = vehicleDetails["type"]
        let vehicleBrand : String = vehicleDetails["brand"]
        let vehicleModel : String = vehicleDetails["model"]
        let plateNumber : String = vehicleDetails["plateNumber"] 
        let fuel : String = vehicleDetails["fuel"] 
        let description : String = vehicleDetails["description"] 
        let location : String = vehicleDetails["location"] 
        let longitude : String = vehicleDetails["longitude"] 
        let latitude : String = vehicleDetails["latitude"] 
        let price : String = vehicleDetails["price"] 
        let timeFrame : String = vehicleDetails["timeFrame"] 
        let listed : String = '1'
        const [result] = await pool.query(`INSERT INTO vehicle_details (VehicleType, VehicleBrand, VehicleModel, PlateNumber, Fuel, Description, Location, Longitude, Latitude, Price, TimeFrame, Listed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);`, [vehicleType, vehicleBrand, vehicleModel, plateNumber, fuel, description, location, longitude, latitude, price, timeFrame, listed]);

        const vehicleID = result.insertId
        await pool.query(`INSERT INTO vehicle_and_owner (VehicleID, UserID) VALUES (?, ?);`, [vehicleID, userID])
    //     return true
    // } catch {
    //     return false
    // }
}

export const uploadVehicleImages = async (images : any, plateNumber : String, type : String) => {
    try {
        plateNumber = plateNumber.substring(1, plateNumber.length - 1)
        type = type.substring(1, type.length - 1)
        const [result] = await pool.query(`SELECT vehicleID FROM vehicle_details WHERE plateNumber = ?;`, plateNumber)
        let vehicleID : any = result[0].vehicleID
        images.map(async (image : any, idx : Number) => {
            let imagePath = await image.path
            let bool = '0'
            if (idx === 0){
                bool = '1'
            }
            await pool.query(`INSERT INTO vehicle_image (ImagePath, VehicleID, MainImage, VehicleType) VALUES (?, ?, ?, ?);`, [imagePath, vehicleID, bool, type])
        })
        return true
    } catch {
        return false
    }
}
export const getVehiclesFromDatabase = async () => {
    try {
        let [result] : Array<any> = await pool.query(`SELECT * FROM vehicle_details;`)
        let l = '1';
        let [mainImage] : Array<any> = await pool.query(`SELECT * FROM vehicle_image WHERE mainImage = ?`, l)
        return {"vehicleDetails": result, "mainImage": mainImage}
    } catch {
        return false
    }
}
export const getVehiclesTypeFromDatabase = async (type: String) => {
    try {
        let [result] : Array<any> = await pool.query(`SELECT * FROM vehicle_details WHERE VehicleType = ?;`, type)
        let l = '1';
        let [mainImage] : Array<any> = await pool.query(`SELECT * FROM vehicle_image WHERE mainImage = ? and VehicleType = ?`, [l, type])
        return {"vehicleDetails": result, "mainImage": mainImage}
    } catch {
        return false
    }
}
export const getVehiclesDetailsFromDatabase = async (id: String) => {
    // try {
        let [result] : Array<any> = await pool.query(`SELECT * FROM vehicle_details WHERE VehicleID = ?;`, id)
        let [mainImage] : Array<any> = await pool.query(`SELECT * FROM vehicle_image WHERE VehicleID = ?`, id)
        let [userID] : Array<any> = await pool.query(`SELECT UserID FROM vehicle_and_owner WHERE VehicleID = ?`, id)
        if(userID[0]){
            userID = userID[0].UserID
        }else{
            userID = 0
        }
        let [userName] : Array<any> = await pool.query(`SELECT Username FROM user_login_data WHERE UserID = ?`, userID)
        
        let vehicleDetails = result
        vehicleDetails[0]["UserID"] = userID
        vehicleDetails[0]["UserName"] = (userName[0]) ? userName[0].Username : ""


        return {vehicleDetails, "mainImage": mainImage}
    // } catch {
    //     return false
    // }
}
export const getBestVehiclesFromDatabase = async () => {
    try {
        let [result] : Array<any> = await pool.query(`SELECT * FROM vehicle_details ORDER BY timesRented DESC LIMIT 5;`)
        let l = '1';
        let [mainImage] : Array<any> = await pool.query(`SELECT * FROM vehicle_image WHERE mainImage = ?`, l)
        return {"vehicleDetails": result, "mainImage": mainImage}
    } catch {
        return false
    }
}
