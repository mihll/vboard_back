package com.mkierzkowski.vboard_back.service.token;

import com.mkierzkowski.vboard_back.model.token.Token;
import com.mkierzkowski.vboard_back.model.token.TokenType;
import com.mkierzkowski.vboard_back.model.user.User;

import java.util.Optional;

public interface TokenService {

    Token createToken(User user, TokenType type);

    Optional<Token> getTokenForUserOfType(User user, TokenType type);

    void deleteToken(Token token);

    Iterable<Token> getAllTokensOfType(TokenType type);

    User getUserForValidTokenOfType(String token, TokenType wantedType);
}
