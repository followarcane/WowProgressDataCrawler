package followarcane.wowdatacrawler.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Data
@Entity(name = "character_info")
@ToString(exclude = {"raiderIOData", "raidProgressions"})
public class CharacterInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String guild;
    private String region;
    private String realm;
    private String ranking;
    @Column(length = 2000)
    private String commentary;
    private String languages;

    @JsonIgnore
    private boolean isRussian = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "raider_io_data_id", referencedColumnName = "id")
    private RaiderIOData raiderIOData;

    @OneToMany(mappedBy = "characterInfo", cascade = CascadeType.ALL)
    private List<RaidProgression> raidProgressions;
}