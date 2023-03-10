package com.eddicorp.application.controller;

import com.eddicorp.application.service.posts.Post;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TemplateTest {
    // jt5 단축키?
    // Junit5 test -> 테스팅 기반 프레임워크(자바8 이상 사용 가능)

    @DisplayName("Mustache Test")
    @Test
    void test(){
        //index 템플릿 읽어볼것임
        final String templateString =
                "      <!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Simple Blog</title>\n" +
                "    <link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\">\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"assets/css/main.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<header id=\"main-nav\">\n" +
                "    <a class=\"main-logo\" href=\"/\">블로그</a>\n" +
                "    <ul>\n" +
                "        <li><a href=\"/\">글 목록</a></li>\n" +
                "        <li><a href=\"write-post.html\">작성하기</a></li>\n" +
                "    </ul>\n" +
                "                {{#isLoggedIn}}\n" +
                "    <a class=\"main-account-setting\" href=\"/logout\">\n" +
                "        <img src=\"assets/icon/logout.svg\">\n" +
                "    </a>\n" +
                "                {{/isLoggedIn}}\n" +
                "        {{^isLoggedIn}}\n" +
                "    <a class=\"main-account-setting\" href=\"login.html\">\n" +
                "        <img src=\"assets/icon/manage_accounts_white_24dp.svg\">\n" +
                "    </a>\n" +
                "                {{/isLoggedIn}}\n" +
                "</header>\n" +
                "<article id=\"main-post\">\n" +
                "                {{#posts}}\n" +
                "    <div class=\"post-box\">\n" +
                "        <h1>{{title}}</h1>\n" +
                "        <h3>작성자: {{author}}</h3>\n" +
                "        <p>\n" +
                "                {{content}}\n" +
                "                </p>\n" +
                "    </div>\n" +
                "                {{/posts}}\n" +
                "</article>\n" +
                "</body>\n" +
                "</html>";

        // 컴파일러가 필요하고 템플릿을 컴파일해줄 것임
        final Mustache.Compiler compiler = Mustache.compiler();
        final Template template = compiler.compile(templateString);

        final Post post1 = new Post("익명", "제목", "내용");
        final Post post2 = new Post("name", "title", "content");

        // list 에 post 들 저장!
        final ArrayList<Post> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);

        // context 에 넘겨줘야함
        final Map<String, Object> context = new HashMap<>();
        context.put("posts", posts);
        // 로그인 안 된 상태에서 테스트
        context.put("isLoggIn", false);
        // context 를 넘겨주면 렌더링 된 String이 나옴
        final String rendered = template.execute(context);
        // 어떻게 렌더링 되는지 확인!
        System.out.println(rendered);
        // #posts 와 #isLoggIn 에 posts list 에 저장한 것들이 렌더링 되어 나옴
    }
}