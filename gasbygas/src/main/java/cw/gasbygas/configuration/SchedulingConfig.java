/*package cw.gasbygas.configuration;

import cw.gasbygas.domain.TokenStatus;
import cw.gasbygas.service.TokenService;
import cw.gasbygas.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig {
    private final TokenService tokenService;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 0 * * *") // Run at midnight every day
    public void handleExpiredTokens() {
        tokenService.getExpiredTokens()
                .forEach(token -> {
                    token.setStatus(TokenStatus.EXPIRED);
                    tokenService.updateTokenStatus(token.getId(), TokenStatus.EXPIRED);
                });
    }

    @Scheduled(cron = "0 0 9 * * *") // Run at 9 AM every day
    public void sendPickupReminders() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0);
        tokenService.getUpcomingPickups(tomorrow)
                .forEach(token ->
                        emailService.sendReminderEmail(token.getGasRequest().getUser().getEmail(), token));
    }
}
*/