package com.mkierzkowski.vboard_back.service.post;

import com.mkierzkowski.vboard_back.dto.request.post.CommentPostRequestDto;
import com.mkierzkowski.vboard_back.dto.request.post.CreatePostRequestDto;
import com.mkierzkowski.vboard_back.dto.request.post.UpdatePostRequestDto;
import com.mkierzkowski.vboard_back.model.post.Post;
import com.mkierzkowski.vboard_back.model.post.PostComment;
import com.mkierzkowski.vboard_back.model.post.PostLike;

import java.util.List;

public interface PostService {

    Post createPost(CreatePostRequestDto createPostRequestDto);

    void updatePost(Long postId, UpdatePostRequestDto updatePostRequestDto);

    void deletePost(Long postId);

    List<Post> getAllBoardPosts(Long boardId, Integer page, String sortBy, String direction);

    List<Post> getBoardPostsContainingText(Long boardId, Integer page, String sortBy, String direction, String searchText);

    List<Post> getPinnedBoardPosts(Long boardId);

    void pinPost(Long postId);

    void unpinPost(Long postId);

    List<PostLike> likePost(Long postId);

    List<PostLike> unlikePost(Long postId);

    List<PostComment> getPostComments(Long postId, Integer page);

    List<PostComment> commentPost(Long postId, CommentPostRequestDto commentPostRequestDto);

    List<PostComment> deleteComment(Long postId, Long commentId);

    Post getPostById(Long postId);
}
