package com.mkierzkowski.vboard_back.service.post;

import com.mkierzkowski.vboard_back.dto.request.post.CreatePostRequestDto;
import com.mkierzkowski.vboard_back.dto.request.post.UpdatePostRequestDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.model.post.Post;
import com.mkierzkowski.vboard_back.model.post.PostLike;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.post.PostLikeRepository;
import com.mkierzkowski.vboard_back.repository.post.PostRepository;
import com.mkierzkowski.vboard_back.service.board.BoardService;
import com.mkierzkowski.vboard_back.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    UserService userService;

    @Autowired
    BoardService boardService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public Post createPost(CreatePostRequestDto createPostRequestDto) {
        BoardMember currentBoardMember = boardService.getBoardMemberOfCurrentUserForBoardId(createPostRequestDto.getBoardId());

        Post postToCreate = new Post(currentBoardMember, createPostRequestDto.getPostText());

        return postRepository.saveAndFlush(postToCreate);
    }

    @Override
    public void updatePost(Long postId, UpdatePostRequestDto updatePostRequestDto) {
        Post postToUpdate = getPostById(postId);

        User currentUser = userService.getCurrentUser();

        if (isPostAuthor(postToUpdate, currentUser)) {
            postToUpdate.setPostText(updatePostRequestDto.getPostText());

            postRepository.saveAndFlush(postToUpdate);
        } else {
            throw VBoardException.throwException(EntityType.POST_UPDATE_REQUEST, ExceptionType.FORBIDDEN, currentUser.getUserId().toString(), postId.toString());
        }
    }

    @Override
    public void pinPost(Long postId) {
        Post postToPin = getPostById(postId);

        User currentUser = userService.getCurrentUser();

        if (boardService.isBoardAdmin(postToPin.getBoard(), currentUser)) {
            postToPin.setIsPinned(true);
            postRepository.saveAndFlush(postToPin);
        } else {
            throw VBoardException.throwException(EntityType.POST_PIN_REQUEST, ExceptionType.FORBIDDEN, currentUser.getUserId().toString(), postId.toString(), postToPin.getBoard().getBoardId().toString());
        }
    }

    @Override
    public void unpinPost(Long postId) {
        Post postToUnpin = getPostById(postId);

        User currentUser = userService.getCurrentUser();

        if (boardService.isBoardAdmin(postToUnpin.getBoard(), currentUser)) {
            postToUnpin.setIsPinned(false);
            postRepository.saveAndFlush(postToUnpin);
        } else {
            throw VBoardException.throwException(EntityType.POST_PIN_REQUEST, ExceptionType.FORBIDDEN, currentUser.getUserId().toString(), postId.toString(), postToUnpin.getBoard().getBoardId().toString());
        }
    }

    @Override
    public List<PostLike> likePost(Long postId) {
        Post postToLike = getPostById(postId);

        //checks if user is board member of board on which postToLike is posted
        BoardMember currentUserBoardMember = boardService.getBoardMemberOfCurrentUserForBoardId(postToLike.getBoard().getBoardId());

        //checks if user did already like this post
        if (postToLike.getPostLikes().stream()
                .anyMatch(postLike -> postLike.getId().getUserId().equals(currentUserBoardMember.getId().getUserId()))) {
            throw VBoardException.throwException(EntityType.POST_LIKE, ExceptionType.DUPLICATE_ENTITY, currentUserBoardMember.getUser().getUserId().toString(), postId.toString());
        }

        PostLike postLikeEntity = new PostLike(currentUserBoardMember.getUser(), postToLike);

        postLikeRepository.saveAndFlush(postLikeEntity);

        return postToLike.getPostLikes();
    }

    @Override
    public List<PostLike> unlikePost(Long postId) {
        Post postToUnlike = getPostById(postId);

        //checks if user is board member of board on which postToLike is posted
        BoardMember currentUserBoardMember = boardService.getBoardMemberOfCurrentUserForBoardId(postToUnlike.getBoard().getBoardId());

        //find current users post like of this post to delete
        PostLike postLikeToDelete = postToUnlike.getPostLikes().stream()
                .filter(postLike -> postLike.getId().getUserId().equals(currentUserBoardMember.getId().getUserId()))
                .findAny()
                .orElseThrow(() -> VBoardException.throwException(EntityType.POST_LIKE, ExceptionType.ENTITY_NOT_FOUND, currentUserBoardMember.getUser().getUserId().toString(), postId.toString()));

        currentUserBoardMember.getUser().getUserPostLikes().remove(postLikeToDelete);
        postToUnlike.getPostLikes().remove(postLikeToDelete);
        postLikeRepository.delete(postLikeToDelete);

        return postToUnlike.getPostLikes();
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> VBoardException.throwException(EntityType.POST, ExceptionType.ENTITY_NOT_FOUND, postId.toString()));
    }

    private boolean isPostAuthor(Post post, User user) {
        return post.getBoardMember().getId().getUserId().equals(user.getUserId());
    }
}
