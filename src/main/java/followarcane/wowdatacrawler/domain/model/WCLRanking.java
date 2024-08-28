package followarcane.wowdatacrawler.domain.model;

import lombok.Data;

@Data
public class WCLRanking {
    private Integer rank;
    private Integer best;
    private Integer totalParses;
    private Double rankPercent;
}
