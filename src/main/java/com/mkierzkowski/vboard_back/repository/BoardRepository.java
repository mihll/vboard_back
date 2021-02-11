package com.mkierzkowski.vboard_back.repository;

import com.mkierzkowski.vboard_back.model.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findBoardByBoardName(String boardName);

    List<Board> findBoardByBoardNameContainingIgnoreCase(String boardName);
}
