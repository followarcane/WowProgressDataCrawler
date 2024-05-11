package followarcane.wowdatacrawler.domain.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private String realm;
    private String ranking;
}
