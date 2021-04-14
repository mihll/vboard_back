package com.mkierzkowski.vboard_back.repository;

import com.mkierzkowski.vboard_back.model.board.BoardJoinRequest;
import com.mkierzkowski.vboard_back.model.board.BoardMemberKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJoinRequestRepository extends JpaRepository<BoardJoinRequest, BoardMemberKey> {
}
