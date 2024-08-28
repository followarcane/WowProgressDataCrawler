package followarcane.wowdatacrawler.infrastructure.service;

import followarcane.wowdatacrawler.domain.model.WCLRanking;
import followarcane.wowdatacrawler.infrastructure.config.WarcraftLogsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class WarcraftlogsService {
    private final RestTemplate restTemplate;
    private final WarcraftLogsConfig config;

    public List<WCLRanking> getCharacterRankings(String characterName, String serverSlug, String serverRegion) {
        String token = getAccessToken();

        String query = String.format(
                "{ characterData { character(name: \"%s\", serverSlug: \"%s\", serverRegion: \"%s\") { encounterRankings } } }",
                characterName, serverSlug, serverRegion
        );


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("query", query);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        String url = config.getUrl();
        Object response = restTemplate.postForObject(url, request, Object.class);
        log.info("Response: {}", response);

        return null;

        //return List.of();
    }


    public String getAccessToken() {
        // OAuth token alma işlemi için gerekli parametreler
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("grant_type", "client_credentials");
        bodyParams.add("client_id", config.getClientId());
        bodyParams.add("client_secret", config.getClientSecret());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(bodyParams, headers);

        // Token URL'i config'den alınıyor
        String tokenUrl = config.getTokenUrl();

        // POST isteği ile token'ı alıyoruz
        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, String> responseBody = response.getBody();
            return responseBody != null ? responseBody.get("access_token") : null;
        } else {
            throw new RuntimeException("Failed to retrieve access token, status code: " + response.getStatusCode());
        }
    }

}
