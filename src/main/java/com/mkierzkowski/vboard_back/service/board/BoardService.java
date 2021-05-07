package com.mkierzkowski.vboard_back.service.board;

import com.mkierzkowski.vboard_back.dto.request.board.ChangeBoardOrderRequestDto;
import com.mkierzkowski.vboard_back.dto.request.board.CreateBoardRequestDto;
import com.mkierzkowski.vboard_back.dto.request.board.UpdateBoardRequestDto;
import com.mkierzkowski.vboard_back.dto.response.board.info.BoardInfoResponseDto;
import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.board.BoardJoinRequest;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.model.post.Post;

import java.util.List;

public interface BoardService {

    Board createBoard(CreateBoardRequestDto createBoardRequestDto);

    Board updateBoard(Long boardId, UpdateBoardRequestDto updateBoardRequestDto);

    BoardInfoResponseDto requestBoardJoin(Long boardId);

    void acceptJoinRequest(Long boardId, Long userId);

    void denyJoinRequest(Long boardId, Long userId);

    void revertBoardJoin(Long boardId);

    void leaveBoard(Long boardId, Long userId);

    void restoreBoardMember(Long boardId, Long userId);

    void grantAdmin(Long boardId, Long userId);

    void revokeAdmin(Long boardId, Long userId);

    BoardMember getBoardMemberOfCurrentUserForId(Long boardId);

    List<Post> getBoardPosts(Long boardId);

    List<BoardMember> getBoardMembers(Long boardId);

    List<BoardJoinRequest> getBoardJoinRequests(Long boardId);

    List<BoardMember> getJoinedBoardsOfCurrentUser();

    List<BoardJoinRequest> getRequestedBoardsOfCurrentUser();

    void changeBoardOrder(ChangeBoardOrderRequestDto changeBoardOrderRequestDto);

    List<BoardInfoResponseDto> findPublicBoardsByName(String boardNameToSearchFor);

    Board getBoardById(Long boardId);
}
