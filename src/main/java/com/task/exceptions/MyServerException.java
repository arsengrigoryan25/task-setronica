package com.task.exceptions;

public class MyServerException extends Exception {

    private static final long serialVersionUID = -116637650240324507L;

    public MyServerException(String errorMessage) {
        super(errorMessage);
    }

}
