package followarcane.wowdatacrawler.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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