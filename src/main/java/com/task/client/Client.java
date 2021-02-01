package com.task.client;

import com.task.dto.*;
import com.task.exceptions.MyClientException;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.task.util.WriterStreamUtility.createObjectOutputStream;
import static com.task.util.ReaderStreamUtility.createObjectInputStream;

public class Client {
    private static final Logger log = Logger.getLogger(Client.class);
    private static final AtomicInteger ordinalNumber = new AtomicInteger(0);
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object remoteCall(String serviceName, String methodName, Object[] params) throws IOException, MyClientException {
        AtomicInteger atomicInteger = new AtomicInteger(ordinalNumber.incrementAndGet());
        Request request =  new Request(atomicInteger, serviceName, methodName, params);

        Socket socket = new Socket(host, port);
        log.info("Client sent command: " + request.toString());
        createObjectOutputStream(socket).writeObject(request);

        ObjectInputStream objectInputStream = createObjectInputStream(socket);
        Callable<Response> callableTask = () -> {
            while(true){
                Response result = (Response) objectInputStream.readObject();
                int result1 = result.getOrdinalNumber().intValue();
                int result2 = request.getOrdinalNumber().intValue();
                if (result1 == result2){
                    return result;
                }
            }
        };

        Future<Response> future = executorService.submit(callableTask);
        Object result = null;
        try {
            result = future.get().getResultAnswer();
            Response response = future.get();
            log.info("Client got a answer: " + response);

            if(response.getCode() != null && !response.getCode().equals("00")){
                String message = "Code: " + response.getCode() + "Message:" + response.getMessage();
                throw new MyClientException(message);
            }
            if(response.getCode() != null && response.getCode().equals("00")){
                result = "Code: " + response.getCode() + "Message:" + response.getMessage();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        socket.close();
        return result;
    }
}