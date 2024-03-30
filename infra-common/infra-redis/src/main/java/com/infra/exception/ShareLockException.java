package com.infra.exception;

public class ShareLockException extends RuntimeException{

    public ShareLockException(String message){
        super(message);
    }

}
