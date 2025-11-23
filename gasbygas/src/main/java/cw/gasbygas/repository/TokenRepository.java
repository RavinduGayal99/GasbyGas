package cw.gasbygas.repository;

import cw.gasbygas.domain.TokenStatus;
import cw.gasbygas.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    List<Token> findByStatus(TokenStatus status);

    @Query("SELECT t FROM Token t WHERE t.pickupDate BETWEEN :start AND :end")
    List<Token> findByPickupDateBetween(LocalDateTime start, LocalDateTime end);
}
