package com.mkierzkowski.vboard_back.repository.post;

import com.mkierzkowski.vboard_back.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
