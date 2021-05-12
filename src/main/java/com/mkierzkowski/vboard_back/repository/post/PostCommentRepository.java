package com.mkierzkowski.vboard_back.repository.post;

import com.mkierzkowski.vboard_back.model.post.Post;
import com.mkierzkowski.vboard_back.model.post.PostComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    List<PostComment> findAllByPost(Post post, Pageable pageable);
}
