package followarcane.wowdatacrawler.infrastructure.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.model.RaidProgression;
import followarcane.wowdatacrawler.domain.model.RaiderIOData;
import followarcane.wowdatacrawler.infrastructure.repository.RaiderIODataRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class RaiderIOService {

    private final RaiderIODataRepository raiderIODataRepository;
    private final TranslationService translationService;

    @Value("${properties.raiderio.url}")
    private String raiderIOUrl;

    // Sadece istediğimiz raid'lerin listesi
    private static final List<String> CURRENT_RAIDS = Arrays.asList(
            "liberation-of-undermine",
            "nerubar-palace"
    );

    public void saveAll(List<RaiderIOData> raiderIODataList) {
        raiderIODataRepository.saveAll(raiderIODataList);
    }

    public void deleteAll() {
        raiderIODataRepository.deleteAll();
    }

    private String calculateBestProgression(Map<String, Object> raidData) {
        int totalBosses = (int) raidData.get("total_bosses");
        int mythicKills = (int) raidData.get("mythic_bosses_killed");
        int heroicKills = (int) raidData.get("heroic_bosses_killed");
        int normalKills = (int) raidData.get("normal_bosses_killed");

        // Önce Mythic kontrol
        if (mythicKills > 0) {
            return String.format("%d/%dM", mythicKills, totalBosses);
        }

        // Sonra Heroic kontrol
        if (heroicKills > 0) {
            return String.format("%d/%dH", heroicKills, totalBosses);
        }

        // En son Normal kontrol
        if (normalKills > 0) {
            return String.format("%d/%dN", normalKills, totalBosses);
        }

        // Hiç kill yoksa 0/totalBossesN formatında döndür
        return String.format("0/%dN", totalBosses);
    }

    @SneakyThrows
    public RaiderIOData fetchRaiderIOData(CharacterInfo info) {
        String realm = info.getRealm();
        String name = info.getName();

        URI uri = UriComponentsBuilder.fromHttpUrl(raiderIOUrl)
                .queryParam("region", info.getRegion())
                .queryParam("realm", realm)
                .queryParam("name", name)
                .queryParam("fields", "raid_progression")
                .build()
                .encode()
                .toUri();

        RaiderIOData partialData = new RaiderIOData();
        partialData.setName(info.getName());
        partialData.setRegion(info.getRegion());
        partialData.setRealm(info.getRealm());

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            if (response.getStatusCodeValue() == 200 && response.hasBody()) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                partialData = mapper.readValue(response.getBody(), RaiderIOData.class);

                Map<String, Object> responseBodyMap = mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
                });
                String raidProgressionJson = mapper.writeValueAsString(responseBodyMap.get("raid_progression"));

                Map<String, Map<String, Object>> raidProgressionMap = mapper.readValue(raidProgressionJson, new TypeReference<Map<String, Map<String, Object>>>() {
                });

                List<RaidProgression> raidProgressions = new ArrayList<>();
                for (Map.Entry<String, Map<String, Object>> entry : raidProgressionMap.entrySet()) {
                    if (CURRENT_RAIDS.contains(entry.getKey())) {
                        RaidProgression raidProgression = new RaidProgression();
                        raidProgression.setRaidName(formatRaidName(entry.getKey()));
                        raidProgression.setSummary(calculateBestProgression(entry.getValue()));
                        raidProgression.setCharacterInfo(info);
                        raidProgressions.add(raidProgression);
                    }
                }
                info.setRaidProgressions(raidProgressions);
                Thread.sleep(300);
            }
        } catch (RestClientException | IOException e) {
            log.error("Failed to fetch data from Raider IO for character: " + info.getName() + ". \nRequest URI: " + uri, e);
        }
        return partialData;
    }

    private String formatRaidName(String raidName) {
        String[] words = raidName.split("-");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
        }
        return String.join(" ", words);
    }
}

