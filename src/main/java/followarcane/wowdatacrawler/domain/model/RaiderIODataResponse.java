package followarcane.wowdatacrawler.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RaiderIODataResponse {
    private String race;
    private String gender;
    private String faction;
    private String classType;
    private String thumbnailUrl;
    private String activeSpecName;
    private String activeSpecRole;
    private String achievementPoints;
}