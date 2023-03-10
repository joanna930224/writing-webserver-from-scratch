package com.eddicorp.application.controller;

import com.eddicorp.http.HttpMethod;
import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;

import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class RootController implements Controller {

    private static final String STATIC_FILE_PATH = "pages";
    private static final Map<RequestMapper, Controller> requestMap = new HashMap<>();

    // method 요청 구분하기
    static {
        final RequestMapper mapWritePost = new RequestMapper("/post", HttpMethod.POST);
        requestMap.put(mapWritePost, new WritePostApiController());
        final RequestMapper mapIndexPage = new RequestMapper("/", HttpMethod.GET);
        requestMap.put(mapIndexPage, new IndexController());
        final RequestMapper mapSignUp = new RequestMapper("/users", HttpMethod.POST);
        requestMap.put(mapSignUp, new SignUpApiController());
        final RequestMapper mapLogin = new RequestMapper("/login", HttpMethod.POST);
        requestMap.put(mapLogin, new SignInApiController());
        final RequestMapper mapLogout = new RequestMapper("/logout", HttpMethod.GET);
        requestMap.put(mapLogout, new LogoutApiController());
    }

    private final Controller staticFileController = new StaticFileController();
    private final Controller notFoundController = new NotFoundController();


    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        // @GetMapping("/users")
        // method = GET
        // uri - "/users"
        final String uri = request.getUri();

        // Request mapper 를 통해서
        final RequestMapper requestMapper = new RequestMapper(uri, request.getHttpMethod());
        //Controller map 에서 조회하면 컨트롤러가 위치할 것 임
        final Controller maybeController = requestMap.get(requestMapper);
        System.out.println("maybeController = " + maybeController);
        // null 이 아니면 처리할 컨트롤러가 있다는 것이니 처리해주면 됨
        if (maybeController != null) {
            maybeController.handle(request, response);
            return;
        }

        final String pathToLoad = Paths.get(STATIC_FILE_PATH, uri).toString();
        System.out.println("pathToLoad = " + pathToLoad);
        final URL maybeResource = this.getClass().getClassLoader().getResource(pathToLoad);
        System.out.println("maybeResource = " + maybeResource);

        // staticFileController 실행
        if (maybeResource != null) {
            staticFileController.handle(request, response);
            return;
        }

        notFoundController.handle(request, response);
    }
}
