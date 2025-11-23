package cw.gasbygas.service;

import cw.gasbygas.domain.TokenStatus;
import cw.gasbygas.model.Token;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public interface TokenService {
    Token generateToken(Date pickupDate);
    void updateTokenStatus(int tokenId, TokenStatus status);
    List<Token> getExpiredTokens();
    List<Token> getUpcomingPickups(LocalDateTime date);
}
