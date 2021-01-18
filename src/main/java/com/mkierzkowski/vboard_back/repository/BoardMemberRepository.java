package com.mkierzkowski.vboard_back.repository;

import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.model.board.BoardMemberKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardMemberRepository extends JpaRepository<BoardMember, BoardMemberKey> {
}
