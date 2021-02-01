package com.task.client;

import com.task.exceptions.MyClientException;

import java.io.IOException;
import java.util.logging.Logger;

public class MyClient{
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 8080);
        for(int i = 0; i < 10; i++) {
            new Thread(new Caller(client)).start();
        }
    }

    private static class Caller implements Runnable {
        private static final Logger logger = Logger.getLogger(Caller.class.getName());
        private Client c;
        public Caller(Client c) {
            this.c = c;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    c.remoteCall("service1", "sleep", new Object[] {new Long(1000)});
                    logger.info("Current Date is:" + c.remoteCall("service1", "getCurrentDate", new Object[] {}));
                } catch (IOException | MyClientException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

