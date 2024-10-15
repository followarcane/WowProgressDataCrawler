package followarcane.wowdatacrawler.api.v1.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BossRankResponse {
    private String encounterName;
    private Double rankPercent;
}