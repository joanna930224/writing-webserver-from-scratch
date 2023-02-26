package com.eddicorp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.cert.CRL;

public class WebApplication {

    public static void main(String[] args) throws Exception {
        // index.html 브라우저에 띄워보기
        final ServerSocket serverSocket = new ServerSocket(8080);
        Socket connection;
        while ((connection = serverSocket.accept()) != null) {
            try (
                    final InputStream inputStream = connection.getInputStream();
                    final OutputStream outputStream = connection.getOutputStream()
            ) {
                final byte[] rawFileToServe = readFileFromResourceStream();

                // 응답 만들기
                // 1. 상태라인
                // - HTTP/1.1 200 OK
                final String CRLF = "\r\n";
                final String statusLine = "HTTP/1.1 200 OK" + CRLF;
                outputStream.write(statusLine.getBytes(StandardCharsets.UTF_8));
                // 2. 헤더
                // - Content-Type: text/html; charset=utf-8
                final String contentTypeHeader = "Content-Type: text/html; charset=utf-8" + CRLF;
                outputStream.write(contentTypeHeader.getBytes(StandardCharsets.UTF_8));
                // - Content-Length: rawFileToServe.length
                final String contentLengthHeader = "Content-Length: " + rawFileToServe.length + CRLF;
                outputStream.write(contentLengthHeader.getBytes(StandardCharsets.UTF_8));
                outputStream.write(CRLF.getBytes(StandardCharsets.UTF_8));
                // 3. 바디
                outputStream.write(rawFileToServe);

                // 이렇게 하면 제대로 화면에는 안나옴.
                // 그 이유는 index.html 이외에 다른 친구들을 줘야하는데 대응이 안되어 있기 때문
            }
        }
    }

    private static byte[] readFileFromResourceStream() throws IOException {
        try (
                final InputStream resourceInputStream = WebApplication.class
                        .getClassLoader()
                        .getResourceAsStream("pages/index.html");
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ) {
            if (resourceInputStream == null) {
                return null;
            }

            int readCount = 0;
            final byte[] readBuffer = new byte[8192];
            while ((readCount = resourceInputStream.read(readBuffer)) != -1) {
                baos.write(readBuffer, 0, readCount);
            }
            return baos.toByteArray();
        }
    }
}
