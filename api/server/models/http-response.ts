export class HttpResponse {
    code: number;
    errorMessage: any;
    constructor(message : any, errorCode : number){
        this.errorMessage = message
        this.code = errorCode
    }
}
// module.exports = HttpResponse