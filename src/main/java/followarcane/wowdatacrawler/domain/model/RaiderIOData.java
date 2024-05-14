package followarcane.wowdatacrawler.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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

    @JsonProperty("name")
    private String name;

    @JsonProperty("race")
    private String race;

    @JsonProperty("class")
    private String characterClass;

    @JsonProperty("active_spec_name")
    private String activeSpecName;

    @JsonProperty("active_spec_role")
    private String activeSpecRole;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("faction")
    private String faction;

    @JsonProperty("achievement_points")
    private int achievementPoints;

    @JsonProperty("honorable_kills")
    private int honorableKills;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("region")
    private String region;

    @JsonProperty("realm")
    private String realm;

    @JsonProperty("last_crawled_at")
    private String lastCrawledAt;

    @JsonProperty("profile_url")
    private String profileUrl;

    @JsonProperty("profile_banner")
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
            raidProgressions = new ObjectMapper().readValue(raidProgressionsJson, new TypeReference<Map<String, RaidProgression>>() {
            });
        }
    }
}