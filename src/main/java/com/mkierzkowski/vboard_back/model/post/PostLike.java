package com.mkierzkowski.vboard_back.model.post;

import com.mkierzkowski.vboard_back.config.auditing.Auditable;
import com.mkierzkowski.vboard_back.model.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostLike extends Auditable<String> {

    @EmbeddedId
    PostLikeKey id;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    Post post;

    public PostLike(User user, Post post) {
        this.id = new PostLikeKey(user.getUserId(), post.getPostId());

        this.user = user;
        this.post = post;

        user.getUserPostLikes().add(this);
        post.getPostLikes().add(this);
    }

    public PostLike() {

    }
}
