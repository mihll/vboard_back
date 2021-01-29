package com.mkierzkowski.vboard_back.service.board;

import com.mkierzkowski.vboard_back.dto.request.board.CreateBoardRequestDto;
import com.mkierzkowski.vboard_back.dto.response.board.CreateBoardResponseDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.BoardMemberRepository;
import com.mkierzkowski.vboard_back.repository.BoardRepository;
import com.mkierzkowski.vboard_back.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserService userService;

    @Autowired
    BoardMemberRepository boardMemberRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CreateBoardResponseDto createBoard(CreateBoardRequestDto createBoardRequestDto) {
        Board boardToCreate = modelMapper.map(createBoardRequestDto, Board.class);
        Optional<Board> sameNameBoard = Optional.ofNullable(boardRepository.findBoardByBoardName(boardToCreate.getBoardName()));

        if (sameNameBoard.isPresent()) {
            throw VBoardException.throwException(EntityType.BOARD, ExceptionType.DUPLICATE_ENTITY, boardToCreate.getBoardName());
        }

        boardToCreate = boardRepository.saveAndFlush(boardToCreate);

        User userFormContext = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User boardCreator = userService.findUserById(userFormContext.getUserId());
        BoardMember boardMember = new BoardMember(boardCreator, boardToCreate, false, true);
        boardMemberRepository.saveAndFlush(boardMember);

        return modelMapper.map(boardToCreate, CreateBoardResponseDto.class);
    }

    @Override
    public List<BoardMember> getBoardsByToken() {
        User userFormContext = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userFromDB = userService.findUserById(userFormContext.getUserId());
        return userFromDB.getJoinedBoards();
    }
}
