package followarcane.wowdatacrawler.domain.converter;

import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.model.CharacterInfoDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CharacterInfoResponseConverter {
    public static CharacterInfoDTO convert(CharacterInfo characterInfo) {
        return CharacterInfoDTO.builder()
                .name(characterInfo.getName())
                .guild(characterInfo.getGuild().isEmpty() ? "No Guild" : characterInfo.getGuild())
                .raid(characterInfo.getRaid())
                .realm(characterInfo.getRealm())
                .ranking(characterInfo.getRanking())
                .build();
    }

    public List<CharacterInfoDTO> convert(List<CharacterInfo> listCharacterInfo) {
        return listCharacterInfo.stream()
                .map(CharacterInfoResponseConverter::convert)
                .toList();
    }
}
