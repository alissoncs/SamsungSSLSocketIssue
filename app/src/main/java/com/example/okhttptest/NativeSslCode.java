package com.example.okhttptest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class NativeSslCode {

    public static void createRequest() {
        try {
            String params = URLEncoder.encode("channel", "UTF-8") + "=" + URLEncoder.encode("rc", "UTF-8");
            params += "&" + URLEncoder.encode("appID", "UTF-8") + "=" + URLEncoder.encode("sicredimobi", "UTF-8");
            params += "&" + URLEncoder.encode("serviceID", "UTF-8") + "=" + URLEncoder.encode("datahora", "UTF-8");

            String host = "api.twitter.com";
            String path = "/1.1/search/tweets.json";

            SSLSocket socket = createSocket(host, 443);

            postData(host, path, params, socket);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SSLSocket createSocket(String host, int port) throws IOException {
        SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(host, port);

        return socket;
    }

    private static void postData(String host, String path, String params, SSLSocket socket) throws IOException {
        // Get input and output streams for the socket
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        String request = "POST " + path + " HTTP/1.0\r\n" + "Accept: */*\r\n" + "Host: " + host + "\r\n"
                + "Connection: Keep-Alive\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n" + "Content-Length: " + params.length()
                + "\r\n\r\n" + params;
//        mConnection.setRequestProperty("Connection", "Keep-Alive");

        // Send off HTTP POST request
        out.write(request.getBytes());
        out.flush();

        // Reads the server's response
        StringBuffer response = new StringBuffer();
        byte[] buffer = new byte[4096];
        int bytes_read;
        System.out.println(socket.getSession().getProtocol());
        System.out.println(socket.getSession().getCipherSuite());


        // Reads HTTP response
        while ((bytes_read = in.read(buffer, 0, 4096)) != -1) {
            // Print server's response
            for (int i = 0; i < bytes_read; i++)
                response.append((char) buffer[i]);
        }

        if (response.substring(response.indexOf(" ") + 1, response.indexOf(" ") + 4).equals("200")) {
            PrintWriter printWriter = new PrintWriter(System.out);
            printWriter.println(response.substring(response.indexOf("\r\n\r\n") + 4));
            printWriter.close();
        } else
            System.out.println("HTTP request failed");
    }
}
