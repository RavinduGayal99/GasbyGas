package cw.gasbygas.service.impl;

import cw.gasbygas.domain.TokenStatus;
import cw.gasbygas.model.Token;
import cw.gasbygas.repository.TokenRepository;
import cw.gasbygas.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public Token generateToken(Date pickupDate) {
        Token token = new Token();
        token.setStatus(TokenStatus.ASSIGNED);
        token.setPickupDate(pickupDate);
        return tokenRepository.save(token);
    }

    private String generateUniqueCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public void updateTokenStatus(int tokenId, TokenStatus status) {

    }

    @Override
    public List<Token> getExpiredTokens() {
        return List.of();
    }

    @Override
    public List<Token> getUpcomingPickups(LocalDateTime date) {
        return List.of();
    }
}
