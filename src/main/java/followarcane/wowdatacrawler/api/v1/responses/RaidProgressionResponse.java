package followarcane.wowdatacrawler.api.v1.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RaidProgressionResponse {
    private String raidName;
    private String summary;
}