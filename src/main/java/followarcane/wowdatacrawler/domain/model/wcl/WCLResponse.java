package followarcane.wowdatacrawler.domain.model.wcl;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class WCLResponse {
    private Character characterData;

    @Data
    public static class Character {
        private long id;
        private String name;
        private int level;
        private int guildRank;
        private Server server;
        private Map<Integer, ZoneData> zones; // Dinamik olarak zone'ları ID'ye göre yönet
    }

    @Data
    public static class Server {
        private String name;
    }

    @Data
    public static class ZoneData {
        private double bestPerformanceAverage;
        private double medianPerformanceAverage;
        private int difficulty;
        private String metric;
        private int partition;
        private int zone;
        private List<AllStars> allStars;
        private List<Ranking> rankings;
    }

    @Data
    public static class AllStars {
        private int partition;
        private String spec;
        private double points;
        private double possiblePoints;
        private int rank;
        private int regionRank;
        private int serverRank;
        private double rankPercent;
        private int total;
    }

    @Data
    public static class Ranking {
        private Encounter encounter;
        private double rankPercent;
        private double medianPercent;
        private boolean lockedIn;
        private int totalKills;
        private long fastestKill;
        private AllStars allStars;
        private String spec;
        private String bestSpec;
        private double bestAmount;
    }

    @Data
    public static class Encounter {
        private int id;
        private String name;
    }
}