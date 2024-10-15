package followarcane.wowdatacrawler.domain.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class BossRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String encounterName;
    private Double rankPercent;

    @ManyToOne
    @JoinColumn(name = "character_info_id", referencedColumnName = "id")
    private CharacterInfo characterInfo;
}
