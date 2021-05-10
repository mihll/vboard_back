package com.mkierzkowski.vboard_back.controller;

import com.mkierzkowski.vboard_back.dto.request.post.CreatePostRequestDto;
import com.mkierzkowski.vboard_back.dto.request.post.UpdatePostRequestDto;
import com.mkierzkowski.vboard_back.dto.response.post.CreatePostResponseDto;
import com.mkierzkowski.vboard_back.dto.response.post.LikePostResponseDto;
import com.mkierzkowski.vboard_back.dto.response.post.boardPosts.BoardPostResponseDto;
import com.mkierzkowski.vboard_back.model.post.Post;
import com.mkierzkowski.vboard_back.model.post.PostLike;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.service.post.PostService;
import com.mkierzkowski.vboard_back.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<CreatePostResponseDto> createPost(@RequestBody @Valid CreatePostRequestDto createPostRequestDto) {
        Post createdPost = postService.createPost(createPostRequestDto);
        CreatePostResponseDto responseDto = modelMapper.map(createdPost, CreatePostResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{postId:.+}")
    public ResponseEntity<BoardPostResponseDto> getPostById(@PathVariable Long postId) {
        Post requestedPost = postService.getPostById(postId);
        User currentUser = userService.getCurrentUser();
        BoardPostResponseDto responseDto = modelMapper.map(requestedPost, BoardPostResponseDto.class);
        responseDto.setIsLiked(requestedPost.isLikedByUser(currentUser));
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{postId:.+}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody @Valid UpdatePostRequestDto updatePostRequestDto) {
        postService.updatePost(postId, updatePostRequestDto);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{postId:.+}/pin")
    public ResponseEntity<?> pinPost(@PathVariable Long postId) {
        postService.pinPost(postId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{postId:.+}/unpin")
    public ResponseEntity<?> unpinPost(@PathVariable Long postId) {
        postService.unpinPost(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId:.+}/like")
    public ResponseEntity<LikePostResponseDto> likePost(@PathVariable Long postId) {
        List<PostLike> postLikes = postService.likePost(postId);
        LikePostResponseDto responseDto = new LikePostResponseDto();
        responseDto.setPostLikesCount(postLikes.size());
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/{postId:.+}/unlike")
    public ResponseEntity<LikePostResponseDto> unlikePost(@PathVariable Long postId) {
        List<PostLike> postLikes = postService.unlikePost(postId);
        LikePostResponseDto responseDto = new LikePostResponseDto();
        responseDto.setPostLikesCount(postLikes.size());
        return ResponseEntity.ok(responseDto);
    }
}
