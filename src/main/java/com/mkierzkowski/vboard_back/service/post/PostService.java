package com.mkierzkowski.vboard_back.service.post;

import com.mkierzkowski.vboard_back.dto.request.post.CreatePostRequestDto;
import com.mkierzkowski.vboard_back.dto.request.post.UpdatePostRequestDto;
import com.mkierzkowski.vboard_back.model.post.Post;
import com.mkierzkowski.vboard_back.model.post.PostLike;

import java.util.List;

public interface PostService {

    Post createPost(CreatePostRequestDto createPostRequestDto);

    void updatePost(Long postId, UpdatePostRequestDto updatePostRequestDto);

    void pinPost(Long postId);

    void unpinPost(Long postId);

    List<PostLike> likePost(Long postId);

    List<PostLike> unlikePost(Long postId);

    Post getPostById(Long postId);
}
