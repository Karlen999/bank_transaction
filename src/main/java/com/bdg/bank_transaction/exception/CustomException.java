package com.bdg.bank_transaction.exception;

public class CustomException extends Exception{

    private static final long serialVersionUID = 1L;

    public CustomException(String msg) {
        super(msg);
    }

    public CustomException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
