package com.adanali.taskee.exception;

public class UserNotFoundException extends ServiceException{

    public UserNotFoundException(String email){
        super("User with email " + email + " does not exists.");
    }

    public UserNotFoundException(Long id){
        super("User with id " + id + " does not exists.");
    }
}
