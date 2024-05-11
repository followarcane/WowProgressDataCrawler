package followarcane.wowdatacrawler.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;


@Data
@Entity(name = "raider_io_data")
public class RaiderIOData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String race;
    @JsonProperty("class")
    private String characterClass;
    private String roleClass;
    private String activeSpecName;
    private String activeSpecRole;
    private String gender;
    private String faction;
    private int achievementPoints;
    private int honorableKills;
    private String thumbnailUrl;
    private String region;
    private String realm;
    private String lastCrawledAt;
    private String profileUrl;
    private String profileBanner;

    @Lob
    @Column(columnDefinition = "text")
    private String raidProgressionsJson;

    @Transient  // ignore this field when saving to the database
    private Map<String, RaidProgression> raidProgressions;

    // call this before save
    public void serializeRaidProgressions() throws JsonProcessingException {
        if (raidProgressions != null) {
            raidProgressionsJson = new ObjectMapper().writeValueAsString(raidProgressions);
        }
    }

    // call this after load
    public void deserializeRaidProgressions() throws JsonProcessingException {
        if (raidProgressionsJson != null) {
            raidProgressions = new ObjectMapper().readValue(raidProgressionsJson, new TypeReference<Map<String, RaidProgression>>(){});
        }
    }
}
