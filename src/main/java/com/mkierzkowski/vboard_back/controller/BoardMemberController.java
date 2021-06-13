package com.mkierzkowski.vboard_back.controller;

import com.mkierzkowski.vboard_back.dto.response.board.info.BoardInfoResponseDto;
import com.mkierzkowski.vboard_back.service.boardMember.BoardMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boardMember")
public class BoardMemberController {

    @Autowired
    BoardMemberService boardMemberService;

    @PostMapping("/{boardId:.+}/joinRequest/accept")
    public ResponseEntity<?> acceptJoinRequest(@PathVariable Long boardId, @RequestParam Long userId) {
        boardMemberService.acceptJoinRequest(boardId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId:.+}/joinRequest/deny")
    public ResponseEntity<?> denyJoinRequest(@PathVariable Long boardId, @RequestParam Long userId) {
        boardMemberService.denyJoinRequest(boardId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId:.+}/join")
    public ResponseEntity<BoardInfoResponseDto> joinBoard(@PathVariable Long boardId) {
        BoardInfoResponseDto responseDto = boardMemberService.requestBoardJoin(boardId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/{boardId:.+}/revertJoin")
    public ResponseEntity<?> revertBoardJoin(@PathVariable Long boardId) {
        boardMemberService.revertBoardJoin(boardId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId:.+}/leave")
    public ResponseEntity<?> leaveBoard(@PathVariable Long boardId, @RequestParam Long userId) {
        boardMemberService.leaveBoard(boardId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId:.+}/restoreMember")
    public ResponseEntity<?> restoreBoardMember(@PathVariable Long boardId, @RequestParam Long userId) {
        boardMemberService.restoreBoardMember(boardId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardId:.+}/deleteMember")
    public ResponseEntity<?> deleteBoardMember(@PathVariable Long boardId, @RequestParam Long userId) {
        boardMemberService.deleteBoardMember(boardId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId:.+}/grantAdmin")
    public ResponseEntity<?> grantAdmin(@PathVariable Long boardId, @RequestParam Long userId) {
        boardMemberService.grantAdmin(boardId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId:.+}/revokeAdmin")
    public ResponseEntity<?> revokeAdmin(@PathVariable Long boardId, @RequestParam Long userId) {
        boardMemberService.revokeAdmin(boardId, userId);
        return ResponseEntity.ok().build();
    }

}
