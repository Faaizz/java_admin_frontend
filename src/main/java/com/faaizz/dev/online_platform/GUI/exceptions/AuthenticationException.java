package com.faaizz.dev.online_platform.GUI.exceptions;

public class AuthenticationException extends Exception{

    public AuthenticationException(){
        super("Please login as staff");
    }

    public AuthenticationException(String message){
        super(message);
    }

}
