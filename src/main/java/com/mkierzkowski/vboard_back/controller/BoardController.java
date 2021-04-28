package com.mkierzkowski.vboard_back.controller;

import com.mkierzkowski.vboard_back.dto.request.board.ChangeBoardOrderRequestDto;
import com.mkierzkowski.vboard_back.dto.request.board.CreateBoardRequestDto;
import com.mkierzkowski.vboard_back.dto.response.board.CreateBoardResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.info.BoardInfoResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.info.GetBoardsResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.members.BoardMemberInfoResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.members.GetBoardMembersResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.my.GetMyBoardsResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.my.MyBoardInfoResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.my.links.GetMyBoardsLinksResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.my.links.JoinedBoardLinkInfoResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.my.requested.GetMyRequestedBoardsResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.my.requested.RequestedBoardInfoResponseDto;
import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.board.BoardJoinRequest;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.service.board.BoardService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<CreateBoardResponseDto> createBoard(@RequestBody @Valid CreateBoardRequestDto createBoardRequestDto) {
        Board createdBoard = boardService.createBoard(createBoardRequestDto);
        CreateBoardResponseDto responseDto = modelMapper.map(createdBoard, CreateBoardResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/{boardId:.+}/join")
    public ResponseEntity<BoardInfoResponseDto> joinBoard(@PathVariable Long boardId) {
        BoardInfoResponseDto responseDto = boardService.requestBoardJoin(boardId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/{boardId:.+}/revertJoin")
    public ResponseEntity<?> revertBoardJoin(@PathVariable Long boardId) {
        boardService.revertBoardJoin(boardId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId:.+}/leave")
    public ResponseEntity<?> leaveBoard(@PathVariable Long boardId, @RequestParam Long userId) {
        boardService.leaveBoard(boardId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/changeOrder")
    public ResponseEntity<?> changeBoardOrder(@RequestBody @Valid ChangeBoardOrderRequestDto changeBoardOrderRequestDto) {
        boardService.changeBoardOrder(changeBoardOrderRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{boardId:.+}")
    public ResponseEntity<MyBoardInfoResponseDto> getBoard(@PathVariable Long boardId) {
        BoardMember joinedBoard = boardService.getBoardOfCurrentUserForId(boardId);
        MyBoardInfoResponseDto responseDto = modelMapper.map(joinedBoard, MyBoardInfoResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{boardId:.+}/members")
    public ResponseEntity<GetBoardMembersResponseDto> getBoardMembers(@PathVariable Long boardId) {
        List<BoardMember> boardMembers = boardService.getBoardMembers(boardId);
        GetBoardMembersResponseDto responseDto = new GetBoardMembersResponseDto();

        responseDto.setMembers(boardMembers
                .stream()
                .map(boardMember -> modelMapper.map(boardMember, BoardMemberInfoResponseDto.class))
                .collect(Collectors.toList()));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/my")
    public ResponseEntity<GetMyBoardsResponseDto> getMyBoards() {
        List<BoardMember> joinedBoards = boardService.getJoinedBoardsOfCurrentUser();
        GetMyBoardsResponseDto responseDto = new GetMyBoardsResponseDto();

        responseDto.setBoards(joinedBoards
                .stream()
                .map(boardMember -> modelMapper.map(boardMember, MyBoardInfoResponseDto.class))
                .collect(Collectors.toList()));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/my/links")
    public ResponseEntity<GetMyBoardsLinksResponseDto> getMyBoardsLinks() {
        List<BoardMember> joinedBoards = boardService.getJoinedBoardsOfCurrentUser();
        GetMyBoardsLinksResponseDto responseDto = new GetMyBoardsLinksResponseDto();

        responseDto.setBoardLinks(joinedBoards
                .stream()
                .map(boardMember -> {
                    Board currentBoard = boardMember.getBoard();
                    return modelMapper.map(currentBoard, JoinedBoardLinkInfoResponseDto.class);
                })
                .collect(Collectors.toList()));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/my/requested")
    public ResponseEntity<GetMyRequestedBoardsResponseDto> getMyRequestedBoards() {
        List<BoardJoinRequest> requestedBoards = boardService.getRequestedBoardsOfCurrentUser();
        GetMyRequestedBoardsResponseDto responseDto = new GetMyRequestedBoardsResponseDto();

        responseDto.setRequestedBoards(requestedBoards
                .stream()
                .map(requestedBoard -> modelMapper.map(requestedBoard, RequestedBoardInfoResponseDto.class))
                .collect(Collectors.toList()));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/findByName")
    public ResponseEntity<GetBoardsResponseDto> findBoardsByName(@RequestParam("name") String name) {
        List<BoardInfoResponseDto> foundBoards = boardService.findPublicBoardsByName(name);
        GetBoardsResponseDto responseDto = new GetBoardsResponseDto();

        responseDto.setBoards(foundBoards);

        return ResponseEntity.ok(responseDto);
    }
}
