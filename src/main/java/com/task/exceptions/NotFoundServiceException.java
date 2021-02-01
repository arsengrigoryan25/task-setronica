package com.task.exceptions;

public class NotFoundServiceException extends MyServerException {

    private static final long serialVersionUID = -6465363144220582803L;
    private static final String MESSAGE = "Not found service ";

    public NotFoundServiceException(String typeName) {
        super(MESSAGE + typeName);
    }

}
