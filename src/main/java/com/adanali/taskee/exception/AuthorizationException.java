package com.adanali.taskee.exception;

public class AuthorizationException extends ServiceException{
    public AuthorizationException(){
        super("Access Denied: The User is not authorized.");
    }
}
