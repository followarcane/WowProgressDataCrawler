package followarcane.wowdatacrawler.domain.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.model.RaidProgression;
import followarcane.wowdatacrawler.domain.model.RaiderIOData;
import followarcane.wowdatacrawler.domain.repository.RaidProgressionRepository;
import followarcane.wowdatacrawler.domain.repository.RaiderIODataRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class RaiderIOService {
    private final RaiderIODataRepository raiderIODataRepository;
    private final RaidProgressionRepository raidProgressionRepository;

    public void saveAll(List<RaiderIOData> raiderIODataList) {
        raiderIODataRepository.saveAll(raiderIODataList);
    }

    public void deleteAll() {
        raiderIODataRepository.deleteAll();
    }

    @SneakyThrows
    public RaiderIOData fetchRaiderIOData(CharacterInfo info) {
        String url = "https://raider.io/api/v1/characters/profile";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("region", info.getRegion())
                .queryParam("realm", info.getRealm().replace(" ", "-"))
                .queryParam("name", info.getName())
                .queryParam("fields", "raid_progression");

        String requestUrl = builder.build().toUriString();

        RaiderIOData partialData = new RaiderIOData();
        // Set the fields directly from the character info
        partialData.setName(info.getName());
        partialData.setRegion(info.getRegion());
        partialData.setRealm(info.getRealm());

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);

            if (response.getStatusCodeValue() == 200 && response.hasBody()) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                partialData = mapper.readValue(response.getBody(), RaiderIOData.class);

                // Get the raid_progression field from the response body
                Map<String, Object> responseBodyMap = mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>(){});
                String raidProgressionJson = mapper.writeValueAsString(responseBodyMap.get("raid_progression"));

                // Parse the raidProgressionJson string into a Map
                Map<String, Map<String, Object>> raidProgressionMap = mapper.readValue(raidProgressionJson, new TypeReference<Map<String, Map<String, Object>>>(){});

                List<RaidProgression> raidProgressions = new ArrayList<>();
                for (Map.Entry<String, Map<String, Object>> entry : raidProgressionMap.entrySet()) {
                    RaidProgression raidProgression = new RaidProgression();
                    raidProgression.setRaidName(formatRaidName(entry.getKey()));
                    raidProgression.setSummary((String) entry.getValue().get("summary"));
                    raidProgression.setCharacterInfo(info);
                    raidProgressions.add(raidProgression);
                }
                info.setRaidProgressions(raidProgressions);
                Thread.sleep(300);
            }
        } catch (RestClientException | IOException e) {
            log.error("Failed to fetch data from Raider IO for character: " + info.getName() + ". \nRequest URL: " + requestUrl, e);
        }
        // return `partialData` in all cases, even it failed to fetch additional data from RaiderIO
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
