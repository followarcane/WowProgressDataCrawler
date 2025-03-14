package followarcane.wowdatacrawler.infrastructure.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import followarcane.wowdatacrawler.api.v1.responses.SuccessResponse;
import followarcane.wowdatacrawler.domain.model.BossRank;
import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.model.WarcraftLogsData;
import followarcane.wowdatacrawler.infrastructure.repository.WarcraftLogsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarcraftLogsService {

    private final WarcraftLogsRepository warcraftLogsRepository;
    @Value("${properties.warcraftlogs.clientId}")
    private String clientId;

    @Value("${properties.warcraftlogs.clientSecret}")
    private String clientSecret;

    @Value("${properties.warcraftlogs.tokenUrl}")
    private String AUTH_ENDPOINT;

    @Value("${properties.warcraftlogs.url}")
    private String API_ENDPOINT;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final int[] ZONE_IDS = {38}; //26, 31, 33, 35

    public Optional<String> authenticate() {
        String credentials = clientId + ":" + clientSecret;
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + base64Credentials);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(new LinkedMultiValueMap<>(), headers);
        request.getBody().add("grant_type", "client_credentials");

        try {
            ResponseEntity<SuccessResponse> response = restTemplate.exchange(AUTH_ENDPOINT, HttpMethod.POST, request, SuccessResponse.class);
            return Optional.ofNullable(response.getBody()).map(SuccessResponse::getAccessToken);
        } catch (Exception e) {
            log.error("Failed to authenticate with WarcraftLogs", e);
            return Optional.empty();
        }
    }

    public Optional<WarcraftLogsData> fetchCharacterData(String accessToken, CharacterInfo characterInfo) {
        StringBuilder queryString = new StringBuilder("{ characterData { character(serverRegion: \\\"" + characterInfo.getRegion() + "\\\", name: \\\"" + characterInfo.getName() + "\\\", serverSlug: \\\"" + characterInfo.getRealm().replace(" ","-") + "\\\") { id name level guildRank server { name }");


        for (int zoneID : ZONE_IDS) {
            queryString.append(" zone").append(zoneID).append(": zoneRankings(byBracket: false, zoneID: ").append(zoneID).append(", metric: dps) ");
        }

        queryString.append("} } }");

        String requestBody = "{ \"query\": \"" + queryString.toString() + "\" }";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(API_ENDPOINT, HttpMethod.POST, request, String.class);

            log.info("Response Body: {}", response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                WarcraftLogsData wclData = parseWCLResponse(response.getBody(),characterInfo);
                return Optional.of(wclData);
            } else {
                log.warn("Failed to fetch character data: {}", response.getStatusCode());
                return Optional.empty();
            }
        } catch (HttpServerErrorException e) {
            log.error("Server error occurred: {}", e.getResponseBodyAsString(), e);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Failed to fetch character data from WarcraftLogs", e);
            return Optional.empty();
        }
    }

    private WarcraftLogsData parseWCLResponse(String responseBody, CharacterInfo characterInfo) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode characterNode = rootNode.path("data").path("characterData").path("character");

            WarcraftLogsData wclData = new WarcraftLogsData();

            //Undermine
            wclData.setZoneName("zone 42");
            wclData.setMetric("dps");
            wclData.setDifficulty(characterNode.path("zone" + ZONE_IDS[0]).path("difficulty").asText());
            wclData.setBestPerformanceAverage(characterNode.path("zone" + ZONE_IDS[0]).path("bestPerformanceAverage").asDouble());

            List<BossRank> bossRanks = new ArrayList<>();
            JsonNode zoneNode = characterNode.path("zone" + ZONE_IDS[0]);
            if (zoneNode.has("rankings")) {
                JsonNode rankingsNode = zoneNode.path("rankings");
                for (JsonNode ranking : rankingsNode) {
                    BossRank bossRank = new BossRank();
                    bossRank.setEncounterName(ranking.path("encounter").path("name").asText());
                    if (ranking.path("rankPercent").asDouble() == 0)
                        break;
                    bossRank.setRankPercent(ranking.path("rankPercent").asDouble());
                    bossRank.setCharacterInfo(characterInfo);
                    bossRanks.add(bossRank);
                }
            }
            characterInfo.setBossRanks(bossRanks);
            characterInfo.setWarcraftLogsData(wclData);
            return wclData;
        } catch (Exception e) {
            log.error("Failed to parse WCLResponse", e);
            return null;
        }
    }

    public void saveAll(List<WarcraftLogsData> warcraftLogsDataList) {
        warcraftLogsRepository.saveAll(warcraftLogsDataList);
    }
}

