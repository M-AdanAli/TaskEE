package com.adanali.taskee.exception;

public class TaskNotFoundException extends ServiceException{
    public TaskNotFoundException(){
        super("Task not Found.");
    }
}
