package com.task.server;

import com.task.dto.*;
import com.task.exceptions.*;
import com.task.util.PropertyReader;
import com.task.util.ReaderStreamUtility;
import com.task.util.WriterStreamUtility;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class MyServer {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    private static final Map<String, Object> objectMap = Collections.synchronizedMap(new HashMap<>());
    static final Logger log = Logger.getLogger(MyServer.class.getName());

    public static void main(String[] args) {
        createObjectForEachServices();

        int port = 8080;
        try{
            if(args.length > 0){
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException e) {
            log.error("Please enter valid value for the port ");
            return;
        }

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                final Socket socket = serverSocket.accept();
                log.info("Client connected: " + socket);
                executorService.submit(() -> processConnection(socket));
            }
        } catch (IOException e) {
            log.error("Error occurred when created object of class ServerSocket or accept ServerSocket: " + e);
        }
    }

    /**
     * Receive a command and send a response to the client
     *
     * @param socket
     */
    private static void processConnection(Socket socket) {
        try {
            ObjectInputStream objectInputStream = ReaderStreamUtility.createObjectInputStream(socket);
            ObjectOutputStream objectOutputStream = WriterStreamUtility.createObjectOutputStream(socket);

            while (true) {
                try {
                    Request request = (Request) objectInputStream.readObject();
                    executorService.submit(() -> {
                        Response response = callAndCheckMethodsOfService(request);
                        response.setOrdinalNumber(request.getOrdinalNumber());

                        try {
                            objectOutputStream.writeObject(response);
                            log.info("Server sent answer: " + response.toString());
                        } catch (IOException e) {
                            log.info("Error occurred when write of output stream, socket - " + socket + ": " + e);
                        }
                    });
                } catch (ClassNotFoundException e) {
                    log.error("Error occurred when read of input stream, socket - " + socket + ": " + e);
                }
            }
        } catch (IOException e) {
            log.error("Error occurred when create instance of class ObjectInputStream, socket - " + socket + ": " + e);
        }
    }

    /**
     * Called and check methods of service, create the answer for client
     *
     * @param request
     * @return
     */
    private static Response callAndCheckMethodsOfService(Request request) {
        Response response = new Response();
        response.setOrdinalNumber(request.getOrdinalNumber());

        Object o = objectMap.get(request.getServiceName());
        String className = "";
        Class<?> clazz;

        int paramsLength = request.getParams().length;
        Object[] params = null;
        if (paramsLength != 0) {
            params = request.getParams();
        }

        try {
            if (o == null) {
                throw new NotFoundServiceException(request.getServiceName());
            } else {
                clazz = o.getClass();
                className = clazz.getName();
            }

            Class<?>[] parametersClass = getParametersClass(request.getParams());
            Method method = clazz.getDeclaredMethod(request.getMethodName(), parametersClass);

            checkCommandParameters(method, parametersClass, request);
            checkReturnTypeVoidOrNot(method, response, request, className);

            Object result = method.invoke(clazz.getDeclaredConstructor().newInstance(), params);
            response.setResultAnswer(result);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            log.error("Error occurred when created new instance of class " + className + ": " + e);
        } catch (NoSuchMethodException e) {
            String errorMessage = "No such method in " + request.getMethodName() + " method name, or this parameters type: ";
            log.error(errorMessage + e);

            response.setCode(ResponseCodeEnum.NO_SUCH_METHOD.getCode());
            response.setMessage(errorMessage);
        } catch (IncorrectParametersTypeException e) {
            log.error("Error occurred in parsing parameters: {}", e);

            response.setCode(ResponseCodeEnum.INCORRECT_PARAMETERS_TYPE.getCode());
            response.setMessage(e.getMessage());
        } catch (IncorrectParametersCountException e) {
            log.error("Error occurred in count parameters: {}", e);

            response.setCode(ResponseCodeEnum.INCORRECT_PARAMETERS_COUNT.getCode());
            response.setMessage(e.getMessage());
        } catch (NotFoundServiceException e) {
            log.error("Error occurred not found service: {}", e);

            response.setCode(ResponseCodeEnum.SERVICE_NOT_FOUND.getCode());
            response.setMessage(e.getMessage());
        }

        return response;
    }

    /**
     * Check type and count of parameters of called method
     *
     * @param method
     * @param parametersClass - the classes of parameters
     * @param request - contain called method's name and parameters
     * @throws IncorrectParametersCountException
     * @throws IncorrectParametersTypeException
     */
    private static void checkCommandParameters(Method method, Class<?>[] parametersClass, Request request)
            throws IncorrectParametersCountException, IncorrectParametersTypeException {
        int paramsLength = request.getParams().length;
        Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterTypes.length != paramsLength) {
            throw new IncorrectParametersCountException(request.getMethodName());
        }

        for (int i = 0; i < parameterTypes.length; i++) {
            if (!(parameterTypes[0].getName()).equals(parametersClass[0].getName())) {
                throw new IncorrectParametersTypeException(request.getMethodName());
            }
        }
    }

    private static void checkReturnTypeVoidOrNot(Method method, Response response, Request request, String className) {
        if (method.getReturnType().getName().equals("void")) {
            response.setCode(ResponseCodeEnum.RETURN_TYPE_VOID.getCode());
            response.setMessage("The " + request.getMethodName() + " method of " + className + " class's return type is void");
        }
    }

    /**
     * Get classes of parameters, by reflection
     *
     * @param params - parameters of called method
     * @return - the classes array of called methods parameters
     */
    private static Class<?>[] getParametersClass(Object[] params) {
        int paramsLength = params.length;
        Class<?>[] parametersClass = null;
        if (paramsLength != 0) {
            parametersClass = new Class[paramsLength];
            for (int i = 0; i < paramsLength; i++) {
                parametersClass[i] = params[i].getClass();
            }
        }
        return parametersClass;
    }

    /**
     * Read services and classes names of service.property file, and create classes objects
     */
    private static void createObjectForEachServices() {
        final String suffixPath = "com.task.server.services.";
        String className = "";
        try {
            Set<Map.Entry<Object, Object>> serviceNames = PropertyReader.getServicesNameByPropertyFile();
            for (Map.Entry<Object, Object> serviceName : serviceNames) {
                Class<?> clazz = Class.forName(suffixPath + serviceName.getValue().toString());
                className = clazz.getName();
                Object o = clazz.getDeclaredConstructor().newInstance();
                objectMap.put(serviceName.getKey().toString(), o);
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            log.error("Error occurred when created new instance of class " + className + ": " + e);
        } catch (NoSuchMethodException e) {
            log.error("Error occurred when constructor of class " + className + ": " + e);
        } catch (ClassNotFoundException e) {
            log.error("Not found class: " + className);
        } catch (IOException e) {
            log.error(e);
        }
    }
}

