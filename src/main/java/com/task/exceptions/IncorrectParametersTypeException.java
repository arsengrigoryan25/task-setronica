package com.task.exceptions;

public class IncorrectParametersTypeException extends MyServerException {

    private static final long serialVersionUID = 5975958834015955647L;
    private static final String MESSAGE = "The types of parameters are not correct in method ";

    public IncorrectParametersTypeException(String typeName) {
        super(MESSAGE + typeName);
    }

}
