package com.mkierzkowski.vboard_back.service.post;

import com.mkierzkowski.vboard_back.dto.request.post.CreatePostRequestDto;
import com.mkierzkowski.vboard_back.dto.request.post.UpdatePostRequestDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.model.post.Post;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.PostRepository;
import com.mkierzkowski.vboard_back.service.board.BoardService;
import com.mkierzkowski.vboard_back.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    UserService userService;

    @Autowired
    BoardService boardService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public Post createPost(CreatePostRequestDto createPostRequestDto) {
        BoardMember currentBoardMember = boardService.getBoardMemberOfCurrentUserForId(createPostRequestDto.getBoardId());

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
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> VBoardException.throwException(EntityType.POST, ExceptionType.ENTITY_NOT_FOUND, postId.toString()));
    }

    private boolean isPostAuthor(Post post, User user) {
        return post.getBoardMember().getId().getUserId().equals(user.getUserId());
    }
}
