package org.github.babkiniaa.scas.exception;

public class NotFoundUserException extends Exception{

    NotFoundUserException(){}

    public NotFoundUserException(String msg){
        super(msg);
    }
}
