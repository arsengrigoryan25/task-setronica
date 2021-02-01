package com.task.dto;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Response implements Serializable {
    private static final long serialVersionUID = -7936765456554018111L;

    private AtomicInteger ordinalNumber;
    private Object resultAnswer;
    private String message;
    private String code;

    public Response() { }

    public Response(AtomicInteger ordinalNumber, Object resultAnswer, String message, String code) {
        this.ordinalNumber = ordinalNumber;
        this.resultAnswer = resultAnswer;
        this.message = message;
        this.code = code;
    }

    public AtomicInteger getOrdinalNumber() {
        return ordinalNumber;
    }
    public void setOrdinalNumber(AtomicInteger ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    public Object getResultAnswer() {
        return resultAnswer;
    }
    public void setResultAnswer(Object resultAnswer) {
        this.resultAnswer = resultAnswer;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "ordinalNumber=" + ordinalNumber +
                ", resultAnswer=" + resultAnswer +
                ", message='" + message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}