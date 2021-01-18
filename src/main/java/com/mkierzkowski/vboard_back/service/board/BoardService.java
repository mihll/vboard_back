package com.mkierzkowski.vboard_back.service.board;

import com.mkierzkowski.vboard_back.dto.request.board.CreateBoardRequestDto;
import com.mkierzkowski.vboard_back.dto.response.board.CreateBoardResponseDto;
import com.mkierzkowski.vboard_back.model.board.BoardMember;

import java.util.List;

public interface BoardService {

    CreateBoardResponseDto createBoard(CreateBoardRequestDto createBoardRequestDto);

    List<BoardMember> getBoardsByToken();
}
