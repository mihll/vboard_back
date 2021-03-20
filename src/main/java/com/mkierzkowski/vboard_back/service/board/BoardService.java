package com.mkierzkowski.vboard_back.service.board;

import com.mkierzkowski.vboard_back.dto.request.board.ChangeBoardOrderRequestDto;
import com.mkierzkowski.vboard_back.dto.request.board.CreateBoardRequestDto;
import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.board.BoardMember;

import java.util.List;

public interface BoardService {

    Board createBoard(CreateBoardRequestDto createBoardRequestDto);

    void changeBoardOrder(ChangeBoardOrderRequestDto changeBoardOrderRequestDto);

    List<BoardMember> getBoardsOfCurrentUser();

    List<Board> findPublicBoardsByName(String boardNameToSearchFor);
}
