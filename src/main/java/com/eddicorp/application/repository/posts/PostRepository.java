package com.eddicorp.application.repository.posts;

import com.eddicorp.application.service.posts.Post;

import java.util.List;

public interface PostRepository {

    //repository Impl로 요청
    void save(Post post);

    List<Post> findAll();
}
