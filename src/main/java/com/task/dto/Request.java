package com.task.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Request implements Serializable {
    private static final long serialVersionUID = -3026656097438244514L;

    private AtomicInteger ordinalNumber;
    private String serviceName;
    private String methodName;
    private Object[] params;

    public Request() { }
    public Request(AtomicInteger ordinalNumber, String serviceName, String methodName, Object[] params) {
        this.ordinalNumber = ordinalNumber;
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.params = params;
    }

    public AtomicInteger getOrdinalNumber() {
        return ordinalNumber;
    }
    public void setOrdinalNumber(AtomicInteger ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[]
    getParams() {
        return params;
    }
    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "Command{" +
                "ordinalNumber=" + ordinalNumber +
                ", serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}