package com.example.demo.exception;


public class PasswordException extends Exception{

    PasswordException(){}

    public PasswordException(String msg){
        super(msg);
    }
}