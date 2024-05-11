package followarcane.wowdatacrawler.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONObject;
import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.model.RaiderIOData;
import followarcane.wowdatacrawler.domain.repository.RaiderIODataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class RaiderIOService {
    private final RaiderIODataRepository raiderIODataRepository;

    public void parseAndSaveData(JSONObject json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RaiderIOData data = objectMapper.readValue(json.toString(), RaiderIOData.class);
        data.serializeRaidProgressions(); // make sure to serialize Map to JSON before saving
        raiderIODataRepository.save(data);
    }

    public RaiderIOData fetchRaiderIOData(CharacterInfo info) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://raider.io/api/v1/characters/profile";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("region", "eu")
                .queryParam("realm", info.getRealm())
                .queryParam("name", info.getName())
                .queryParam("fields", "raid_progression");

        ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);

        if (response.getStatusCodeValue() == 200 && response.hasBody()) {
            ObjectMapper mapper = new ObjectMapper();
            RaiderIOData data = mapper.readValue(response.getBody(), RaiderIOData.class);
            data.deserializeRaidProgressions();
            return data;
        }
        // handle unsuccessful or empty response here, I just throw a runtime exception for simplicity.
        throw new RuntimeException("Failed to fetch data from Raider IO for character: " + info.getName());
    }
}
