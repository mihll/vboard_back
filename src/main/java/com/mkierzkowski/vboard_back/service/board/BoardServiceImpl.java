package com.mkierzkowski.vboard_back.service.board;

import com.mkierzkowski.vboard_back.dto.request.board.ChangeBoardOrderRequestDto;
import com.mkierzkowski.vboard_back.dto.request.board.CreateBoardRequestDto;
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
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    UserService userService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardMemberRepository boardMemberRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public Board createBoard(CreateBoardRequestDto createBoardRequestDto) {

        Optional<Board> sameNameBoard = boardRepository.findBoardByBoardName(createBoardRequestDto.getBoardName());
        if (sameNameBoard.isPresent()) {
            throw VBoardException.throwException(EntityType.BOARD, ExceptionType.DUPLICATE_ENTITY, createBoardRequestDto.getBoardName());
        }

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        Board boardToCreate = modelMapper.map(createBoardRequestDto, Board.class);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        boardToCreate = boardRepository.saveAndFlush(boardToCreate);

        User boardCreator = userService.getCurrentUser();
        BoardMember boardMember = new BoardMember(boardCreator, boardToCreate, false, true);
        boardMemberRepository.saveAndFlush(boardMember);

        return boardToCreate;
    }

    @Override
    public void changeBoardOrder(ChangeBoardOrderRequestDto changeBoardOrderRequestDto) {

        List<Long> boardIdsInOrder;
        try {
            boardIdsInOrder = changeBoardOrderRequestDto.getBoardIds()
                    .stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw VBoardException.throwException(EntityType.BOARD_LIST, ExceptionType.INVALID, changeBoardOrderRequestDto.getBoardIds().toString());
        }

        Set<Long> receivedBoardIds = new HashSet<>(boardIdsInOrder);

        User currentUser = userService.getCurrentUser();
        Set<Long> userBoardIds = currentUser.getJoinedBoards()
                .stream()
                .map(boardMember ->
                        boardMember.getId().getBoardId())
                .collect(Collectors.toSet());

        if (receivedBoardIds.size() != boardIdsInOrder.size() || !receivedBoardIds.equals(userBoardIds)) {
            throw VBoardException.throwException(EntityType.BOARD_LIST, ExceptionType.INVALID, boardIdsInOrder.toString());
        }

        currentUser.getJoinedBoards().sort(Comparator.comparing(board -> boardIdsInOrder.indexOf(board.getId().getBoardId())));

        userService.saveAndFlushUser(currentUser);
    }

    @Override
    public List<BoardMember> getBoardsOfCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return currentUser.getJoinedBoards();
    }

    @Override
    public List<Board> findPublicBoardsByName(String boardNameToSearchFor) {
        List<Board> foundBoards = boardRepository.findBoardByBoardNameContainingIgnoreCase(boardNameToSearchFor);
        return foundBoards.stream().filter(board -> !board.getIsPrivate()).collect(Collectors.toList());
    }
}
