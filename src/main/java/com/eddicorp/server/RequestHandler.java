package com.eddicorp.server;

import com.eddicorp.application.controller.RootController;
import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// main 에 있던 거 요청 핸들러로 옮겨서 정리함
public class RequestHandler {

    private final RootController rootController = new RootController();

    public void handle(final InputStream inputStream, OutputStream outputStream) {
        try {
            //요청 분석해서 응답할 수 있도록!
            final HttpRequest httpRequest = new HttpRequest(inputStream);
            final HttpResponse httpResponse = new HttpResponse(outputStream);
            rootController.handle(httpRequest, httpResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
