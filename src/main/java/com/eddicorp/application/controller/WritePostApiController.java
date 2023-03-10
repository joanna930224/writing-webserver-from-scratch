package com.eddicorp.application.controller;

import com.eddicorp.application.service.posts.PostService;
import com.eddicorp.application.service.posts.PostServiceImpl;
import com.eddicorp.application.service.users.User;
import com.eddicorp.http.HttpSession;
import com.eddicorp.http.HttpStatus;
import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;

public class WritePostApiController implements Controller {

    private final PostService postService = new PostServiceImpl();

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        //글 작성 title & content 가져오기
        final String title = request.getParameter("title");
        final String content = request.getParameter("content");
        final HttpSession maybeSession = request.getSession();
        String username;
        if (maybeSession == null) {
            username = "익명";
        } else {
            // 로그인 되어있을 때 User 가져오기
            final User user = (User) maybeSession.getAttribute("USER");
            if (user != null && maybeSession.getAttribute("USER") != null) {
                username = user.getUsername();
            } else {
                username = "익명";
            }
        }
        //Post 서비스 불러와서 처리
        postService.write(username, title, content);
        // 글 작성 완료하고 메인으로 돌아가는게 좋으니까 작성 완료된 후에 어떤 작업할 것인지를 지정해주기!

        // httpResponse.sendRedirect("/") -> 리다이렉션 이 형태로 구현해도 되긴하는데
        // 풀어서 작업하는 것을 보여주기 위해 아래와 같이 구현함

        //헤더에 로케이션 정보 넣어줘야함 : headerValue 자리에 오는 것으로 요청하게 됨
        response.setHeader("Location", "/");
        //파운드 셋팅
        response.setStatus(HttpStatus.FOUND);
        // body 는 굳이 필요 없어서 null 로 넣음
        response.renderRawBody(null);
    }
}
