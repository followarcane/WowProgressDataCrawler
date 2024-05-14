package followarcane.wowdatacrawler.domain.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class RaidProgression {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String raidName;
    private String summary;

    @ManyToOne
    @JoinColumn(name = "character_info_id")
    private CharacterInfo characterInfo;
}