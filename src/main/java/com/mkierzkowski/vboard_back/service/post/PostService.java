package com.mkierzkowski.vboard_back.service.post;

import com.mkierzkowski.vboard_back.dto.request.post.CreatePostRequestDto;
import com.mkierzkowski.vboard_back.model.post.Post;

public interface PostService {

    Post createPost(CreatePostRequestDto createPostRequestDto);

    void pinPost(Long postId);

    void unpinPost(Long postId);

    Post getPostById(Long postId);
}
