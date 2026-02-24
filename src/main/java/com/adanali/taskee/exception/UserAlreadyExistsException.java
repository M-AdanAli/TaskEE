package com.adanali.taskee.exception;

public class UserAlreadyExistsException extends ServiceException{
    public UserAlreadyExistsException(String email){
        super("User with email " + email + " already exists.");
    }
}
