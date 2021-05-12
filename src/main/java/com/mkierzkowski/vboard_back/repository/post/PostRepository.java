package com.mkierzkowski.vboard_back.repository.post;

import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByBoard(Board board, Pageable pageable);

    List<Post> findAllByBoardAndPostTextContainingIgnoreCase(Board board, Pageable pageable, String postText);

    List<Post> findAllByBoardAndIsPinned(Board board, boolean isPinned);
}
