package com.mkierzkowski.vboard_back.service.token;

import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.token.Token;
import com.mkierzkowski.vboard_back.model.token.TokenType;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.TokenRepository;
import com.mkierzkowski.vboard_back.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    @Transactional
    public Token createToken(User user, TokenType type) {
        String token = UUID.randomUUID().toString();
        Token myToken = new Token(token, user, type);
        return tokenRepository.save(myToken);
    }

    @Override
    public Optional<Token> getTokenForUserOfType(User user, TokenType type) {
        return tokenRepository.findByUserAndType(user, type);
    }

    @Override
    public void deleteToken(Token token) {
        tokenRepository.delete(token);
    }

    @Override
    public Iterable<Token> getAllTokensOfType(TokenType type) {
        return tokenRepository.findAllByType(type);
    }

    @Override
    @Transactional
    public User getUserForValidTokenOfType(String token, TokenType wantedType) {
        Token foundToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> VBoardException.throwException(EntityType.TOKEN, ExceptionType.ENTITY_NOT_FOUND, token));

        User user = foundToken.getUser();
        TokenType foundType = foundToken.getType();

        if (foundType != wantedType) {
            throw VBoardException.throwException(EntityType.TOKEN, ExceptionType.INVALID, token);
        }

        Calendar cal = Calendar.getInstance();

        if ((foundToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            tokenRepository.delete(foundToken);
            if (foundType == TokenType.VERIFICATION) {
                userService.deleteUser(user);
            }
            throw VBoardException.throwException(EntityType.TOKEN, ExceptionType.EXPIRED, token);
        }

        tokenRepository.delete(foundToken);

        return user;
    }
}
