package com.adanali.taskee.exception;

public class AuthorizationException extends ServiceException{
    public AuthorizationException(){
        this("The User is not authorized.");
    }

    public AuthorizationException(String message){
        super("Access Denied: "+message);
    }
}
