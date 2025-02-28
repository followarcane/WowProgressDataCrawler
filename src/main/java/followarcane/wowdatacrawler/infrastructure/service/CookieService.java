package followarcane.wowdatacrawler.infrastructure.service;

import followarcane.wowdatacrawler.infrastructure.service.version.VersionNotifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class CookieService {

    private final VersionNotifierService versionNotifier;

    // Default çalışan cookie
    private String currentCookie = "xxx";
    private LocalDateTime lastUpdated = LocalDateTime.now();

    public synchronized void updateCookie(String cookie) {
        this.currentCookie = cookie;
        this.lastUpdated = LocalDateTime.now();
        log.info("Cookie updated at: {}", lastUpdated);

        // Discord'a bildirim gönder
        versionNotifier.sendHealthCheck(true, 0,
                String.format("Cookie güncellendi! Tarih: %s", lastUpdated));
    }

    public String getCookie() {
        return currentCookie;
    }

    public String getCookieStatus() {
        if (currentCookie == null) return "No cookie set";
        return String.format("Cookie last updated: %s", lastUpdated);
    }
} 