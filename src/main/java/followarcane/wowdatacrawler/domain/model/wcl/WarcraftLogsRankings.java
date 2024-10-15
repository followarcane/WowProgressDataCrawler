package followarcane.wowdatacrawler.domain.model.wcl;

import lombok.Data;

@Data
public class WarcraftLogsRankings {
    private WarcraftLogsEncounter encounter;
    private double rankPercent;
}
