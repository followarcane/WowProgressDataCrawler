package followarcane.wowdatacrawler.domain.model;

import lombok.Data;

@Data
class RaidProgression {
    private String summary;
    private int totalBosses;
    private int normalBossesKilled;
    private int heroicBossesKilled;
    private int mythicBossesKilled;
}
