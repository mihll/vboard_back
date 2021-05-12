package com.mkierzkowski.vboard_back.service.post;

import com.mkierzkowski.vboard_back.dto.request.post.CommentPostRequestDto;
import com.mkierzkowski.vboard_back.dto.request.post.CreatePostRequestDto;
import com.mkierzkowski.vboard_back.dto.request.post.UpdatePostRequestDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.model.post.Post;
import com.mkierzkowski.vboard_back.model.post.PostComment;
import com.mkierzkowski.vboard_back.model.post.PostLike;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.post.PostCommentRepository;
import com.mkierzkowski.vboard_back.repository.post.PostLikeRepository;
import com.mkierzkowski.vboard_back.repository.post.PostRepository;
import com.mkierzkowski.vboard_back.service.board.BoardService;
import com.mkierzkowski.vboard_back.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    PostCommentRepository postCommentRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuditingHandler auditingHandler;

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
    public List<Post> getAllBoardPosts(Long boardId, Integer page, String sortBy, String direction) {
        // checks if user is member of requested board
        BoardMember currentBoardMemberForBoardId = boardService.getBoardMemberOfCurrentUserForBoardId(boardId);
        Pageable pageRequest;
        if (!sortBy.isEmpty() && !direction.isEmpty()) {
            if (sortBy.equals("postDate")) {
                sortBy = "createdDate";
            } else if (sortBy.equals("lastActivity")) {
                sortBy = "lastModifiedDate";
            } else {
                throw VBoardException.throwException(EntityType.POST, ExceptionType.INVALID, "sortBy = " + sortBy);
            }

            if (direction.equals("asc")) {
                pageRequest = PageRequest.of(page, 10, Sort.by(sortBy).ascending());
            } else if (direction.equals("desc")) {
                pageRequest = PageRequest.of(page, 10, Sort.by(sortBy).descending());
            } else {
                throw VBoardException.throwException(EntityType.POST, ExceptionType.INVALID, "direction = " + direction);
            }

        } else {
            pageRequest = PageRequest.of(page, 10, Sort.by("lastModifiedDate").descending());
        }

        return postRepository.findAllByBoard(currentBoardMemberForBoardId.getBoard(), pageRequest);
    }

    @Override
    public List<Post> getBoardPostsContainingText(Long boardId, Integer page, String sortBy, String direction, String searchText) {
        // checks if user is member of requested board
        BoardMember currentBoardMemberForBoardId = boardService.getBoardMemberOfCurrentUserForBoardId(boardId);
        Pageable pageRequest;
        if (!sortBy.isEmpty() && !direction.isEmpty()) {
            if (sortBy.equals("postDate")) {
                sortBy = "createdDate";
            } else if (sortBy.equals("lastActivity")) {
                sortBy = "lastModifiedDate";
            } else {
                throw VBoardException.throwException(EntityType.POST, ExceptionType.INVALID, "sortBy = " + sortBy);
            }

            if (direction.equals("asc")) {
                pageRequest = PageRequest.of(page, 10, Sort.by(sortBy).ascending());
            } else if (direction.equals("desc")) {
                pageRequest = PageRequest.of(page, 10, Sort.by(sortBy).descending());
            } else {
                throw VBoardException.throwException(EntityType.POST, ExceptionType.INVALID, "direction = " + direction);
            }

        } else {
            pageRequest = PageRequest.of(page, 10, Sort.by("lastModifiedDate").descending());
        }

        return postRepository.findAllByBoardAndPostTextContainingIgnoreCase(currentBoardMemberForBoardId.getBoard(), pageRequest, searchText);
    }

    @Override
    public List<Post> getPinnedBoardPosts(Long boardId) {
        // checks if user is member of requested board
        BoardMember currentBoardMemberForBoardId = boardService.getBoardMemberOfCurrentUserForBoardId(boardId);
        return postRepository.findAllByBoardAndIsPinned(currentBoardMemberForBoardId.getBoard(), true);
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

        auditingHandler.markModified(postToLike);
        postRepository.save(postToLike);

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

        auditingHandler.markModified(postToUnlike);
        postRepository.save(postToUnlike);

        return postToUnlike.getPostLikes();
    }

    @Override
    public List<PostComment> getPostComments(Long postId, Integer page) {
        Post requestedPost = getPostById(postId);

        //checks if user is board member of board on which postToLike is posted
        boardService.getBoardMemberOfCurrentUserForBoardId(requestedPost.getBoard().getBoardId());

        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by("createdDate").descending());

        return postCommentRepository.findAllByPost(requestedPost, pageRequest);
    }

    @Override
    public List<PostComment> commentPost(Long postId, CommentPostRequestDto commentPostRequestDto) {
        Post postToComment = getPostById(postId);

        //checks if user is board member of board on which postToLike is posted
        BoardMember currentUserBoardMember = boardService.getBoardMemberOfCurrentUserForBoardId(postToComment.getBoard().getBoardId());

        PostComment postCommentEntity = new PostComment(postToComment, currentUserBoardMember.getUser(), commentPostRequestDto.getCommentText());

        postCommentRepository.saveAndFlush(postCommentEntity);

        auditingHandler.markModified(postToComment);
        postRepository.save(postToComment);

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        return postCommentRepository.findAllByPost(postToComment, pageRequest);
    }

    @Override
    public List<PostComment> deleteComment(Long postId, Long commentId) {
        Post postToUpdate = getPostById(postId);
        PostComment commentToDelete = getCommentById(commentId);

        //checks if user is board member of board on which postToLike is posted
        BoardMember currentUserBoardMember = boardService.getBoardMemberOfCurrentUserForBoardId(postToUpdate.getBoard().getBoardId());

        if (commentToDelete.getUser().equals(currentUserBoardMember.getUser())) {
            postCommentRepository.delete(commentToDelete);

            PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("createdDate").descending());
            return postCommentRepository.findAllByPost(postToUpdate, pageRequest);
        } else {
            throw VBoardException.throwException(EntityType.POST_COMMENT, ExceptionType.FORBIDDEN, currentUserBoardMember.getUser().getUserId().toString(), commentId.toString());
        }
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> VBoardException.throwException(EntityType.POST, ExceptionType.ENTITY_NOT_FOUND, postId.toString()));
    }

    private PostComment getCommentById(Long commentId) {
        return postCommentRepository.findById(commentId)
                .orElseThrow(() -> VBoardException.throwException(EntityType.POST_COMMENT, ExceptionType.ENTITY_NOT_FOUND, commentId.toString()));
    }

    private boolean isPostAuthor(Post post, User user) {
        return post.getBoardMember().getId().getUserId().equals(user.getUserId());
    }
}
