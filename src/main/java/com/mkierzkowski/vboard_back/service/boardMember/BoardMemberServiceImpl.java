package com.mkierzkowski.vboard_back.service.boardMember;

import com.mkierzkowski.vboard_back.dto.response.board.info.BoardInfoResponseDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.board.BoardJoinRequest;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.board.BoardJoinRequestRepository;
import com.mkierzkowski.vboard_back.repository.board.BoardMemberRepository;
import com.mkierzkowski.vboard_back.service.board.BoardService;
import com.mkierzkowski.vboard_back.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BoardMemberServiceImpl implements BoardMemberService {

    @Autowired
    UserService userService;

    @Autowired
    BoardService boardService;

    @Autowired
    BoardMemberRepository boardMemberRepository;

    @Autowired
    BoardJoinRequestRepository boardJoinRequestRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<BoardMember> getBoardMembers(Long boardId) {
        return getBoardMemberOfCurrentUserForBoardId(boardId).getBoard().getBoardMembers();
    }

    @Override
    public List<BoardJoinRequest> getBoardJoinRequests(Long boardId) {
        Board requestedBoard = boardService.getBoardById(boardId);

        User currentUser = userService.getCurrentUser();

        if (isBoardAdmin(requestedBoard, currentUser)) {
            return requestedBoard.getBoardJoinRequests();
        } else {
            throw VBoardException.throwException(EntityType.BOARD_JOIN_REQUESTS_LIST, ExceptionType.FORBIDDEN, boardId.toString());
        }
    }

    @Override
    @Transactional
    public void acceptJoinRequest(Long boardId, Long userId) {
        Board requestedBoard = boardService.getBoardById(boardId);

        User currentUser = userService.getCurrentUser();

        if (isBoardAdmin(requestedBoard, currentUser)) {
            //checks if join request for requested user exists
            BoardJoinRequest joinRequestToAccept = requestedBoard.getBoardJoinRequests().stream()
                    .filter(joinRequest -> joinRequest.getId().getUserId().equals(userId))
                    .findAny()
                    .orElseThrow(() -> VBoardException.throwException(EntityType.BOARD_JOIN_REQUEST, ExceptionType.ENTITY_NOT_FOUND, userId.toString(), boardId.toString()));

            addUserToBoard(joinRequestToAccept.getBoard(), joinRequestToAccept.getUser(), false);

            boardJoinRequestRepository.delete(joinRequestToAccept);
        } else {
            throw VBoardException.throwException(EntityType.BOARD_JOIN_REQUEST, ExceptionType.FORBIDDEN, boardId.toString());
        }
    }

    @Override
    public void denyJoinRequest(Long boardId, Long userId) {
        Board requestedBoard = boardService.getBoardById(boardId);

        User currentUser = userService.getCurrentUser();

        if (isBoardAdmin(requestedBoard, currentUser)) {
            //checks if join request for requested user exists
            BoardJoinRequest joinRequestToDeny = requestedBoard.getBoardJoinRequests().stream()
                    .filter(joinRequest -> joinRequest.getId().getUserId().equals(userId))
                    .findAny()
                    .orElseThrow(() -> VBoardException.throwException(EntityType.BOARD_JOIN_REQUEST, ExceptionType.ENTITY_NOT_FOUND, userId.toString(), boardId.toString()));

            boardJoinRequestRepository.delete(joinRequestToDeny);
        } else {
            throw VBoardException.throwException(EntityType.BOARD_JOIN_REQUEST, ExceptionType.FORBIDDEN, boardId.toString());
        }
    }

    @Override
    @Transactional
    public BoardInfoResponseDto requestBoardJoin(Long boardId) {
        Board boardToJoin = boardService.getBoardById(boardId);

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
            BoardMember addedUser = addUserToBoard(boardToJoin, currentUser, false);

            return modelMapper.map(addedUser.getBoard(), BoardInfoResponseDto.class)
                    .setIsJoined(true);
        } else {
            BoardJoinRequest boardJoinRequest = new BoardJoinRequest(currentUser, boardToJoin);
            boardJoinRequestRepository.saveAndFlush(boardJoinRequest);

            return modelMapper.map(boardJoinRequest.getBoard(), BoardInfoResponseDto.class)
                    .setIsRequested(true);
        }
    }

    @Override
    public BoardMember addUserToBoard(Board boardToAddTo, User userToAdd, Boolean isAdmin) {

        Optional<BoardMember> existingBoardMember = boardToAddTo.getBoardMembers().stream()
                .filter(joinRequest -> joinRequest.getUser().equals(userToAdd))
                .findAny();

        //checks if user did previously leave that board
        if (existingBoardMember.isPresent()) {
            existingBoardMember.get().setDidLeft(false);
            return boardMemberRepository.saveAndFlush(existingBoardMember.get());
        } else {
            BoardMember boardMember = new BoardMember(userToAdd, boardToAddTo, false, isAdmin);
            return boardMemberRepository.saveAndFlush(boardMember);
        }
    }

    @Override
    @Transactional
    public void revertBoardJoin(Long boardId) {
        Board boardToRevertJoinRequest = boardService.getBoardById(boardId);

        User currentUser = userService.getCurrentUser();

        //checks if user have already requested to join this board
        BoardJoinRequest boardJoinRequestToRevert = currentUser.getRequestedBoards().stream()
                .filter(boardJoinRequest -> boardJoinRequest.getId().getBoardId().equals(boardToRevertJoinRequest.getBoardId()))
                .findAny()
                .orElseThrow(() -> VBoardException.throwException(EntityType.BOARD_JOIN_REQUEST, ExceptionType.ENTITY_NOT_FOUND, currentUser.getUserId().toString(), boardId.toString()));

        currentUser.getRequestedBoards().remove(boardJoinRequestToRevert);
        boardJoinRequestToRevert.getBoard().getBoardJoinRequests().remove(boardJoinRequestToRevert);
        boardJoinRequestRepository.delete(boardJoinRequestToRevert);
    }

    @Override
    @Transactional
    public void leaveBoard(Long boardId, Long userId) {
        Board boardToLeave = boardService.getBoardById(boardId);

        //checks if user to leave is a member of requested board
        BoardMember boardMemberToLeave = userService.findUserById(userId).getJoinedBoards().stream()
                .filter(boardMember -> boardMember.getId().getBoardId().equals(boardId))
                .findAny()
                .orElseThrow(() -> VBoardException.throwException(EntityType.BOARD_LEAVE_REQUEST, ExceptionType.INVALID, userId.toString(), boardId.toString()));

        User currentUser = userService.getCurrentUser();

        //check if current user is requesting leave for himself OR is board admin
        if (currentUser.equals(boardMemberToLeave.getUser())) {

            //check if user is not an only admin of a board
            if (boardMemberToLeave.getIsAdmin() && boardToLeave.getAdmins().count() == 1) {
                throw VBoardException.throwException(EntityType.BOARD_LEAVE_REQUEST, ExceptionType.FAILED, userId.toString(), boardId.toString());
            }

            boardMemberToLeave.setDidLeft(true);
            boardMemberToLeave.setIsAdmin(false);
            boardMemberRepository.saveAndFlush(boardMemberToLeave);

        } else if (isBoardAdmin(boardToLeave, currentUser)) {

            boardMemberToLeave.setDidLeft(true);
            boardMemberToLeave.setIsAdmin(false);
            boardMemberRepository.saveAndFlush(boardMemberToLeave);

        } else {
            throw VBoardException.throwException(EntityType.BOARD_LEAVE_REQUEST, ExceptionType.FORBIDDEN, boardId.toString());
        }
    }

    @Override
    @Transactional
    public void restoreBoardMember(Long boardId, Long userId) {
        Board requestedBoard = boardService.getBoardById(boardId);

        User currentUser = userService.getCurrentUser();

        if (isBoardAdmin(requestedBoard, currentUser)) {

            //checks if user to restore is a member of requested board
            BoardMember boardMemberToRestore = getBoardMembers(boardId).stream()
                    .filter(boardMember -> boardMember.getId().getUserId().equals(userId))
                    .findAny()
                    .orElseThrow(() -> VBoardException.throwException(EntityType.BOARD_MEMBER_RESTORE_REQUEST, ExceptionType.INVALID, userId.toString(), boardId.toString()));

            //checks if user to restore has left the board
            if (!boardMemberToRestore.getDidLeft()) {
                throw VBoardException.throwException(EntityType.BOARD_MEMBER_RESTORE_REQUEST, ExceptionType.FAILED, userId.toString(), boardId.toString());
            }

            //checks if user has sent a request to join the board again and if so deletes the request
            requestedBoard.getBoardJoinRequests().stream()
                    .filter(joinRequest -> joinRequest.getId().getUserId().equals(userId))
                    .findAny()
                    .ifPresent(joinRequest -> boardJoinRequestRepository.delete(joinRequest));

            boardMemberToRestore.setDidLeft(false);
            boardMemberRepository.saveAndFlush(boardMemberToRestore);

        } else {
            throw VBoardException.throwException(EntityType.BOARD_MEMBER_RESTORE_REQUEST, ExceptionType.FORBIDDEN, boardId.toString());
        }

    }

    @Override
    @Transactional
    public void deleteBoardMember(Long boardId, Long userId) {
        Board requestedBoard = boardService.getBoardById(boardId);

        User currentUser = userService.getCurrentUser();

        if (isBoardAdmin(requestedBoard, currentUser)) {
            //checks if user to delete is a member of requested board
            BoardMember boardMemberToDelete = getBoardMembers(boardId).stream()
                    .filter(boardMember -> boardMember.getId().getUserId().equals(userId))
                    .findAny()
                    .orElseThrow(() -> VBoardException.throwException(EntityType.BOARD_MEMBER_DELETE_REQUEST, ExceptionType.INVALID, userId.toString(), boardId.toString()));

            boardMemberRepository.delete(boardMemberToDelete);
        } else {
            throw VBoardException.throwException(EntityType.BOARD_MEMBER_DELETE_REQUEST, ExceptionType.FORBIDDEN, boardId.toString());
        }
    }

    @Override
    public void grantAdmin(Long boardId, Long userId) {
        Board boardToModify = boardService.getBoardById(boardId);

        //checks if user to grant admin is a member of requested board
        BoardMember boardMemberToGrantAdmin = userService.findUserById(userId).getJoinedBoards().stream()
                .filter(boardMember -> boardMember.getId().getBoardId().equals(boardId))
                .findAny()
                .orElseThrow(() -> VBoardException.throwException(EntityType.BOARD_GRANT_ADMIN_REQUEST, ExceptionType.INVALID, userId.toString(), boardId.toString()));

        User currentUser = userService.getCurrentUser();

        //check if current user is board admin
        if (isBoardAdmin(boardToModify, currentUser)) {

            if (boardMemberToGrantAdmin.getIsAdmin()) {
                throw VBoardException.throwException(EntityType.BOARD_GRANT_ADMIN_REQUEST, ExceptionType.FAILED, userId.toString(), boardId.toString());
            }

            boardMemberToGrantAdmin.setIsAdmin(true);
            boardMemberRepository.saveAndFlush(boardMemberToGrantAdmin);

        } else {
            throw VBoardException.throwException(EntityType.BOARD_GRANT_ADMIN_REQUEST, ExceptionType.FORBIDDEN, boardId.toString());
        }
    }

    @Override
    public void revokeAdmin(Long boardId, Long userId) {
        Board boardToModify = boardService.getBoardById(boardId);

        //checks if user to grant admin is a member of requested board
        BoardMember boardMemberToRevokeAdmin = userService.findUserById(userId).getJoinedBoards().stream()
                .filter(boardMember -> boardMember.getId().getBoardId().equals(boardId))
                .findAny()
                .orElseThrow(() -> VBoardException.throwException(EntityType.BOARD_REVOKE_ADMIN_REQUEST, ExceptionType.INVALID, userId.toString(), boardId.toString()));

        User currentUser = userService.getCurrentUser();

        //check if current user is board admin
        if (isBoardAdmin(boardToModify, currentUser)) {

            //check if user is currently admin of requested board OR if is not an only admin of requested board
            if (!boardMemberToRevokeAdmin.getIsAdmin()) {
                throw VBoardException.throwException(EntityType.BOARD_REVOKE_ADMIN_REQUEST, ExceptionType.FAILED, "Requested user (" + userId + ") is not an admin of this board (" + boardId + ").");
            } else if (boardToModify.getAdmins().count() == 1) {
                throw VBoardException.throwException(EntityType.BOARD_REVOKE_ADMIN_REQUEST, ExceptionType.FAILED, "Requested user (" + userId + ") cannot be revoked admin permissions because he is an only admin of this board (" + boardId + ").");
            }

            boardMemberToRevokeAdmin.setIsAdmin(false);
            boardMemberRepository.saveAndFlush(boardMemberToRevokeAdmin);

        } else {
            throw VBoardException.throwException(EntityType.BOARD_REVOKE_ADMIN_REQUEST, ExceptionType.FORBIDDEN, boardId.toString());
        }
    }


    @Override
    public BoardMember getBoardMemberOfCurrentUserForBoardId(Long boardId) {
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
    public boolean isBoardAdmin(Board board, User user) {
        return board.getAdmins()
                .anyMatch(boardAdmin -> boardAdmin.getId().getUserId().equals(user.getUserId()));
    }
}
