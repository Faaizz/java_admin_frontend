package com.faaizz.dev.online_platform.api_outbound.exceptions;

public class ResponseException extends Exception{

    public ResponseException(String reason_phrase, int status_code){

        //CALL CONSTRUCTOR OF PARENT CLASS
        super("Message: " + reason_phrase + "\n" + "Response Code: " + status_code);

    }

}
