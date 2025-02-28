package followarcane.wowdatacrawler.api.v1.controller;

import followarcane.wowdatacrawler.infrastructure.service.CookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cookie")
@RequiredArgsConstructor
public class CookieController {

    private final CookieService cookieService;

    @PostMapping("/update")
    public ResponseEntity<String> updateCookie(@RequestBody String cookie) {
        cookieService.updateCookie(cookie);
        return ResponseEntity.ok("Cookie updated successfully");
    }

    @GetMapping("/status")
    public ResponseEntity<String> getCookieStatus() {
        return ResponseEntity.ok(cookieService.getCookieStatus());
    }
} 