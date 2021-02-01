package com.task.exceptions;

public class IncorrectParametersCountException extends MyServerException {

    private static final long serialVersionUID = -1251470816742334855L;
    private static final String MESSAGE = "The counts of parameters are not correct ";

    public IncorrectParametersCountException(String typeName) {
        super(MESSAGE + typeName);
    }

}
