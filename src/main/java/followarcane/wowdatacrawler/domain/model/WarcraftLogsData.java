package followarcane.wowdatacrawler.domain.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "warcraft_logs_data")
public class WarcraftLogsData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zoneName;
    private String metric;
    private String difficulty;
    private Double bestPerformanceAverage;

//    @ManyToOne
//    @JoinColumn(name = "character_info_id", referencedColumnName = "id")
//    private CharacterInfo characterInfo;
}
