package com.mkierzkowski.vboard_back.repository.post;

import com.mkierzkowski.vboard_back.model.post.PostLike;
import com.mkierzkowski.vboard_back.model.post.PostLikeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeKey> {
}
