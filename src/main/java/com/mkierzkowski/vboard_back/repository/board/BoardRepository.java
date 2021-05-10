package com.mkierzkowski.vboard_back.repository.board;

import com.mkierzkowski.vboard_back.model.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findBoardByBoardName(String boardName);

    List<Board> findBoardByBoardNameContainingIgnoreCase(String boardName);
}
