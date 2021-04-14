package com.mkierzkowski.vboard_back.service.board;

import com.mkierzkowski.vboard_back.dto.request.board.ChangeBoardOrderRequestDto;
import com.mkierzkowski.vboard_back.dto.request.board.CreateBoardRequestDto;
import com.mkierzkowski.vboard_back.dto.response.board.info.BoardInfoResponseDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.board.BoardJoinRequest;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.BoardJoinRequestRepository;
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
    BoardJoinRequestRepository boardJoinRequestRepository;

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
    @Transactional
    public BoardInfoResponseDto requestBoardJoin(Long boardId) {
        Board boardToJoin = boardRepository.findById(boardId)
                .orElseThrow(() -> VBoardException.throwException(EntityType.BOARD, ExceptionType.ENTITY_NOT_FOUND, boardId.toString()));

        User currentUser = userService.getCurrentUser();

        //check if user did already requested join to this board
        if (currentUser.getRequestedBoards().stream()
                .anyMatch(boardJoinRequest -> boardJoinRequest.getId().getBoardId().equals(boardId))) {
            throw VBoardException.throwException(EntityType.BOARD_JOIN_REQUEST, ExceptionType.DUPLICATE_ENTITY, boardId.toString());
        }

        //check if user is not already a member of requested board
        if (currentUser.getJoinedBoards().stream()
                .anyMatch(boardMember -> boardMember.getId().getBoardId().equals(boardId))) {
            throw VBoardException.throwException(EntityType.BOARD_JOIN_REQUEST, ExceptionType.INVALID, boardId.toString());
        }

        //check if board should accept all requesting users
        if (boardToJoin.getAcceptAll()) {
            //TODO: should call method to add user to board
            return null;
        } else {
            BoardJoinRequest boardJoinRequest = new BoardJoinRequest(currentUser, boardToJoin);
            boardJoinRequestRepository.saveAndFlush(boardJoinRequest);

            return modelMapper.map(boardJoinRequest.getBoard(), BoardInfoResponseDto.class)
                    .setIsRequested(true);
        }
    }

    @Override
    @Transactional
    public void revertBoardJoin(Long boardId) {
        Board boardToRevertJoinRequest = boardRepository.findById(boardId)
                .orElseThrow(() -> VBoardException.throwException(EntityType.BOARD, ExceptionType.ENTITY_NOT_FOUND, boardId.toString()));

        User currentUser = userService.getCurrentUser();

        //checks if user have already requested to join this board
        BoardJoinRequest boardJoinRequestToRevert = currentUser.getRequestedBoards().stream()
                .filter(boardJoinRequest -> boardJoinRequest.getId().getBoardId().equals(boardToRevertJoinRequest.getBoardId()))
                .findAny()
                .orElseThrow(() -> VBoardException.throwException(EntityType.BOARD_JOIN_REQUEST, ExceptionType.ENTITY_NOT_FOUND, boardId.toString()));

        currentUser.getRequestedBoards().remove(boardJoinRequestToRevert);
        boardJoinRequestToRevert.getBoard().getBoardJoinRequests().remove(boardJoinRequestToRevert);
        boardJoinRequestRepository.delete(boardJoinRequestToRevert);
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
    public BoardMember getBoardOfCurrentUserForId(Long boardId) {
        return getJoinedBoardsOfCurrentUser().stream()
                .filter(boardMember -> boardMember.getId().getBoardId().equals(boardId))
                .findAny()
                .orElseThrow(() -> VBoardException.throwException(EntityType.BOARD, ExceptionType.FORBIDDEN, boardId.toString()));
    }

    @Override
    public List<BoardMember> getJoinedBoardsOfCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return currentUser.getJoinedBoards();
    }

    @Override
    public List<BoardJoinRequest> getRequestedBoardsOfCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return currentUser.getRequestedBoards();
    }

    @Override
    public List<BoardInfoResponseDto> findPublicBoardsByName(String boardNameToSearchFor) {
        List<Board> foundBoards = boardRepository.findBoardByBoardNameContainingIgnoreCase(boardNameToSearchFor);
        List<BoardMember> currentUserBoards = getJoinedBoardsOfCurrentUser();
        List<BoardJoinRequest> currentUserRequestedBoards = getRequestedBoardsOfCurrentUser();

        return foundBoards
                .stream()
                .filter(board -> !board.getIsPrivate())
                .map(currentBoard -> modelMapper.map(currentBoard, BoardInfoResponseDto.class))
                .peek(currentBoardInfoResponse -> {
                    if (currentUserBoards.stream()
                            .anyMatch(userBoard -> userBoard.getId().getBoardId().toString()
                                    .equals(currentBoardInfoResponse.getBoardId()))) {
                        currentBoardInfoResponse.setIsJoined(true);
                    }
                    if (currentUserRequestedBoards.stream()
                            .anyMatch(requestedBoard -> requestedBoard.getId().getBoardId().toString()
                                    .equals(currentBoardInfoResponse.getBoardId()))) {
                        currentBoardInfoResponse.setIsRequested(true);
                    }
                })
                .collect(Collectors.toList());
    }
}
