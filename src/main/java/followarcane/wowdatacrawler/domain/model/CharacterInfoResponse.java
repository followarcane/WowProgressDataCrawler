package followarcane.wowdatacrawler.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CharacterInfoResponse {
    private String name;
    private String guild;
    private String region;
    private String realm;
    private String iLevel;
    private String commentary;
    private String languages;
    private RaiderIODataResponse raiderIODataResponse;
    private List<RaidProgressionResponse> raidProgressions;
}