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

    void leaveBoard(Long boardId);

    void changeBoardOrder(ChangeBoardOrderRequestDto changeBoardOrderRequestDto);

    BoardMember getBoardOfCurrentUserForId(Long boardId);

    List<BoardMember> getBoardMembers(Long boardId);

    List<BoardMember> getJoinedBoardsOfCurrentUser();

    List<BoardJoinRequest> getRequestedBoardsOfCurrentUser();

    List<BoardInfoResponseDto> findPublicBoardsByName(String boardNameToSearchFor);
}
