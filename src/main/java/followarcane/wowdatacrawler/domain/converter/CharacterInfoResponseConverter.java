package followarcane.wowdatacrawler.domain.converter;

import followarcane.wowdatacrawler.domain.model.CharacterInfoResponse;
import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CharacterInfoResponseConverter {
    public static CharacterInfoResponse convert(CharacterInfo characterInfo) {
        return CharacterInfoResponse.builder()
                .name(characterInfo.getName())
                .guild(characterInfo.getGuild().isEmpty() ? "No Guild" : characterInfo.getGuild())
                .raid(characterInfo.getRaid())
                .realm(characterInfo.getRealm())
                .ranking(characterInfo.getRanking())
                .build();
    }

    public List<CharacterInfoResponse> convert(List<CharacterInfo> listCharacterInfo) {
        return listCharacterInfo.stream()
                .map(CharacterInfoResponseConverter::convert)
                .toList();
    }
}
