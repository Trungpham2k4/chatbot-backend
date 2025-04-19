package com.example.backend_chatbot.service;

import com.example.backend_chatbot.repository.InvalidateTokenRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyJobService {

    private final InvalidateTokenRepo invalidateTokenRepo;

    /**
     * Cron job xóa token hết hạn vào 00:00
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteExpiryToken(){
        invalidateTokenRepo.deleteExpireToken();
        log.info("Delete token at: {}", LocalDateTime.now());
    }
}
