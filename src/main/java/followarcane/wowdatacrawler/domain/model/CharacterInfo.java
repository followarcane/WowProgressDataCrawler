package followarcane.wowdatacrawler.domain.model;

import lombok.*;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Data
@Entity(name = "character_info")
public class CharacterInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String guild;
    private String raid;
    private String region;
    private String realm;
    private String ranking;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "raider_io_data_id", referencedColumnName = "id")
    private RaiderIOData raiderIOData;

}
