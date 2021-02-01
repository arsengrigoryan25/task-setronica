package com.task.util;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriterStreamUtility {

    public static synchronized BufferedWriter createBufferedWriter(Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        return new BufferedWriter(osw);
    }

    public static synchronized ObjectOutputStream createObjectOutputStream(Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        return new ObjectOutputStream(outputStream);
    }

}


