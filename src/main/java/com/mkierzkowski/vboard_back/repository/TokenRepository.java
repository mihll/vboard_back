package com.mkierzkowski.vboard_back.repository;

import com.mkierzkowski.vboard_back.model.token.Token;
import com.mkierzkowski.vboard_back.model.token.TokenType;
import com.mkierzkowski.vboard_back.model.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    Optional<Token> findByUserAndType(User user, TokenType type);

    Iterable<Token> findAllByType(TokenType type);
}
