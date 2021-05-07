package com.mkierzkowski.vboard_back.service.post;

import com.mkierzkowski.vboard_back.dto.request.post.CreatePostRequestDto;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.model.post.Post;
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
}
