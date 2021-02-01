package com.task.util;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ReaderStreamUtility {

    public static synchronized BufferedReader createBufferedReader(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        return new BufferedReader(isr);
    }

    public static synchronized ObjectInputStream createObjectInputStream(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        return new ObjectInputStream(inputStream);
    }

}


