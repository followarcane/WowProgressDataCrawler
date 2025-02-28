package followarcane.wowdatacrawler.infrastructure.service.cloudflare;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlareSolverrService {
    
    private final RestTemplate restTemplate;

    @Value("${properties.flaresolverr.url}")
    private String flaresolverrUrl;

    public String getPageContent(String url) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("cmd", "request.get");
            request.put("url", url);
            request.put("maxTimeout", 60000);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    flaresolverrUrl,
                    request,
                    Map.class
            );

            return (String) ((Map) response.getBody().get("solution")).get("response");
        } catch (Exception e) {
            log.error("Failed to get page content through FlareSolverr: {}", e.getMessage());
            return null;
        }
    }
} 