package com.task.exceptions;

public class MyClientException extends Exception {

    private static final long serialVersionUID = -3510284888470269390L;

    public MyClientException(String errorMessage) {
        super(errorMessage);
    }

}
