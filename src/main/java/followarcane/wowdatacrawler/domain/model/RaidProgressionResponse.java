package followarcane.wowdatacrawler.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RaidProgressionResponse {
    private String raidName;
    private String summary;
}