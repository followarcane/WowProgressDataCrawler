package followarcane.wowdatacrawler.api.v1.Responses;

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
    private RaiderIODataResponse raiderIOData;
    private List<RaidProgressionResponse> raidProgressions;
}