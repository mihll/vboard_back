package com.mkierzkowski.vboard_back.service.boardMember;

import com.mkierzkowski.vboard_back.dto.response.board.info.BoardInfoResponseDto;
import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.board.BoardJoinRequest;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.model.user.User;

import java.util.List;

public interface BoardMemberService {

    List<BoardMember> getBoardMembers(Long boardId);

    List<BoardJoinRequest> getBoardJoinRequests(Long boardId);

    void acceptJoinRequest(Long boardId, Long userId);

    void denyJoinRequest(Long boardId, Long userId);

    BoardInfoResponseDto requestBoardJoin(Long boardId);

    BoardMember addUserToBoard(Board boardToAddTo, User userToAdd, Boolean isAdmin);

    void revertBoardJoin(Long boardId);

    void leaveBoard(Long boardId, Long userId);

    void restoreBoardMember(Long boardId, Long userId);

    void deleteBoardMember(Long boardId, Long userId);

    void grantAdmin(Long boardId, Long userId);

    void revokeAdmin(Long boardId, Long userId);

    BoardMember getBoardMemberOfCurrentUserForBoardId(Long boardId);

    List<BoardMember> getJoinedBoardsOfCurrentUser();

    List<BoardJoinRequest> getRequestedBoardsOfCurrentUser();

    boolean isBoardAdmin(Board board, User user);
}
