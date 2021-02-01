package com.task.server.services;

import java.util.Date;

public class Service1 {
    public void sleep(Long millis) throws InterruptedException {
        Thread.sleep(millis.longValue());
    }
    public Date getCurrentDate() {
        return new Date();
    }
}
