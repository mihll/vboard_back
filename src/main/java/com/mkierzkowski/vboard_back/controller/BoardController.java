package com.mkierzkowski.vboard_back.controller;

import com.mkierzkowski.vboard_back.dto.request.board.CreateBoardRequestDto;
import com.mkierzkowski.vboard_back.dto.response.board.CreateBoardResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.info.BoardInfoResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.info.GetBoardsResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.my.GetMyBoardsResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.my.MyBoardInfoResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.my.links.GetMyBoardsLinksResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.my.links.JoinedBoardLinkInfoResponseDto;
import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.service.board.BoardService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity createBoard(@RequestBody @Valid CreateBoardRequestDto createBoardRequestDto) {
        CreateBoardResponseDto responseDto = boardService.createBoard(createBoardRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/my")
    public ResponseEntity getMyBoards() {
        List<BoardMember> joinedBoards = boardService.getBoardsByToken();
        GetMyBoardsResponseDto responseDto = new GetMyBoardsResponseDto();

        responseDto.setBoards(joinedBoards
                .stream()
                .map(boardMember -> {
                    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
                    return modelMapper.map(boardMember, MyBoardInfoResponseDto.class);
                }).collect(Collectors.toList()));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/my/links")
    public ResponseEntity getMyBoardsLinks() {
        List<BoardMember> joinedBoards = boardService.getBoardsByToken();
        GetMyBoardsLinksResponseDto responseDto = new GetMyBoardsLinksResponseDto();

        responseDto.setBoardLinks(joinedBoards
                .stream()
                .map(boardMember -> {
                    Board currentBoard = boardMember.getBoard();
                    return modelMapper.map(currentBoard, JoinedBoardLinkInfoResponseDto.class);
                }).collect(Collectors.toList()));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/findByName")
    public ResponseEntity findBoardsByName(@RequestParam("name") String name) {
        List<Board> foundBoards = boardService.findPublicBoardsByName(name);
        GetBoardsResponseDto responseDto = new GetBoardsResponseDto();

        responseDto.setBoards(foundBoards
                .stream()
                .map(currentBoard -> modelMapper.map(currentBoard, BoardInfoResponseDto.class)).collect(Collectors.toList()));

        return ResponseEntity.ok(responseDto);
    }

}
