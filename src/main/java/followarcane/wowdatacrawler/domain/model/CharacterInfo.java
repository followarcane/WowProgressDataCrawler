package followarcane.wowdatacrawler.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CharacterInfo {
    private String name;
    private String guild;
    private String raid;
    private String realm;
    private String ranking;
}