package com.mkierzkowski.vboard_back.service.board;

import com.mkierzkowski.vboard_back.dto.request.board.ChangeBoardOrderRequestDto;
import com.mkierzkowski.vboard_back.dto.request.board.CreateBoardRequestDto;
import com.mkierzkowski.vboard_back.dto.response.board.info.BoardInfoResponseDto;
import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.board.BoardJoinRequest;
import com.mkierzkowski.vboard_back.model.board.BoardMember;

import java.util.List;

public interface BoardService {

    Board createBoard(CreateBoardRequestDto createBoardRequestDto);

    BoardInfoResponseDto requestBoardJoin(Long boardId);

    void revertBoardJoin(Long boardId);

    void leaveBoard(Long boardId, Long userId);

    void grantAdmin(Long boardId, Long userId);

    void revokeAdmin(Long boardId, Long userId);

    BoardMember getBoardOfCurrentUserForId(Long boardId);

    List<BoardMember> getBoardMembers(Long boardId);

    List<BoardMember> getJoinedBoardsOfCurrentUser();

    List<BoardJoinRequest> getRequestedBoardsOfCurrentUser();

    void changeBoardOrder(ChangeBoardOrderRequestDto changeBoardOrderRequestDto);

    List<BoardInfoResponseDto> findPublicBoardsByName(String boardNameToSearchFor);
}
