package com.mkierzkowski.vboard_back.service.board;

import com.mkierzkowski.vboard_back.dto.request.board.ChangeBoardOrderRequestDto;
import com.mkierzkowski.vboard_back.dto.request.board.CreateBoardRequestDto;
import com.mkierzkowski.vboard_back.dto.request.board.UpdateBoardRequestDto;
import com.mkierzkowski.vboard_back.dto.response.board.info.BoardInfoResponseDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.board.BoardJoinRequest;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.board.BoardRepository;
import com.mkierzkowski.vboard_back.service.boardMember.BoardMemberService;
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
    BoardMemberService boardMemberService;

    @Autowired
    BoardRepository boardRepository;

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
        boardMemberService.addUserToBoard(boardToCreate, boardCreator, true);

        return boardToCreate;
    }

    @Override
    @Transactional
    public Board updateBoard(Long boardId, UpdateBoardRequestDto updateBoardRequestDto) {

        Board boardToUpdate = getBoardById(boardId);
        Optional<Board> sameNameBoard = boardRepository.findBoardByBoardName(updateBoardRequestDto.getBoardName());

        //check if board with the same name does exist and if yes, if it is other board than the one we are modifying
        if (sameNameBoard.isPresent() && !sameNameBoard.get().equals(boardToUpdate)) {
            throw VBoardException.throwException(EntityType.BOARD, ExceptionType.DUPLICATE_ENTITY, updateBoardRequestDto.getBoardName());
        }

        User currentUser = userService.getCurrentUser();

        if (boardMemberService.isBoardAdmin(boardToUpdate, currentUser)) {
            boardToUpdate.setBoardName(updateBoardRequestDto.getBoardName());
            boardToUpdate.setDescription(updateBoardRequestDto.getDescription());
            boardToUpdate.setIsPrivate(updateBoardRequestDto.getIsPrivate());
            boardToUpdate.setAcceptAll(updateBoardRequestDto.getAcceptAll());
            boardToUpdate.setAddressCity(updateBoardRequestDto.getAddressCity());
            boardToUpdate.setAddressPostCode(updateBoardRequestDto.getAddressPostCode());
            boardToUpdate.setAddressStreet(updateBoardRequestDto.getAddressStreet());

            return boardRepository.saveAndFlush(boardToUpdate);
        } else {
            throw VBoardException.throwException(EntityType.BOARD_UPDATE_REQUEST, ExceptionType.FORBIDDEN, boardId.toString());
        }
    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId) {
        Board boardToDelete = getBoardById(boardId);
        User currentUser = userService.getCurrentUser();

        if (boardMemberService.isBoardAdmin(boardToDelete, currentUser)) {
            currentUser.getJoinedBoardsForModification().remove(boardMemberService.getBoardMemberOfCurrentUserForBoardId(boardId));
            boardRepository.delete(boardToDelete);
        } else {
            throw VBoardException.throwException(EntityType.BOARD_DELETE_REQUEST, ExceptionType.FORBIDDEN, boardId.toString());
        }
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

        currentUser.getJoinedBoardsForModification().sort(Comparator.comparing(board -> boardIdsInOrder.indexOf(board.getId().getBoardId())));

        userService.saveAndFlushUser(currentUser);
    }

    @Override
    public List<BoardInfoResponseDto> findPublicBoardsByName(String boardNameToSearchFor) {
        List<Board> foundBoards = boardRepository.findBoardByBoardNameContainingIgnoreCase(boardNameToSearchFor);
        List<BoardMember> currentUserBoards = boardMemberService.getJoinedBoardsOfCurrentUser();
        List<BoardJoinRequest> currentUserRequestedBoards = boardMemberService.getRequestedBoardsOfCurrentUser();

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

    @Override
    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> VBoardException.throwException(EntityType.BOARD, ExceptionType.ENTITY_NOT_FOUND, boardId.toString()));
    }
}
