package com.faaizz.dev.online_platform.api_outbound.exceptions;

public class ResponseException extends Exception{

    private String api_json_response;

    public ResponseException(String api_json_response, int status_code){

        //CALL CONSTRUCTOR OF PARENT CLASS
        super("Message: " + api_json_response + "\n" + "Response Code: " + status_code);

        this.api_json_response= api_json_response;

    }

    public String getJsonResponse(){
        return this.api_json_response;
    }

}
