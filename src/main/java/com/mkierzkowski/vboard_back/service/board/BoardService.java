package com.mkierzkowski.vboard_back.service.board;

import com.mkierzkowski.vboard_back.dto.request.board.ChangeBoardOrderRequestDto;
import com.mkierzkowski.vboard_back.dto.request.board.CreateBoardRequestDto;
import com.mkierzkowski.vboard_back.dto.request.board.UpdateBoardRequestDto;
import com.mkierzkowski.vboard_back.dto.response.board.info.BoardInfoResponseDto;
import com.mkierzkowski.vboard_back.model.board.Board;

import java.util.List;

public interface BoardService {

    Board createBoard(CreateBoardRequestDto createBoardRequestDto);

    Board updateBoard(Long boardId, UpdateBoardRequestDto updateBoardRequestDto);

    void deleteBoard(Long boardId);

    void changeBoardOrder(ChangeBoardOrderRequestDto changeBoardOrderRequestDto);

    List<BoardInfoResponseDto> findPublicBoardsByName(String boardNameToSearchFor);

    Board getBoardById(Long boardId);
}
