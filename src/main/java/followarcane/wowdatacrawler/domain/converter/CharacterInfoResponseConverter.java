package followarcane.wowdatacrawler.domain.converter;

import followarcane.wowdatacrawler.domain.model.CharacterInfoResponse;
import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CharacterInfoResponseConverter {
    public static CharacterInfo convert(CharacterInfoResponse characterInfoResponse) {
        return CharacterInfo.builder()
                .name(characterInfoResponse.getName())
                .guild(characterInfoResponse.getGuild().isEmpty() ? "No Guild" : characterInfoResponse.getGuild())
                .raid(characterInfoResponse.getRaid())
                .realm(characterInfoResponse.getRealm())
                .ranking(characterInfoResponse.getRanking())
                .build();
    }

    public List<CharacterInfo> convert(List<CharacterInfoResponse> listCharacterInfoResponse) {
        return listCharacterInfoResponse.stream()
                .map(CharacterInfoResponseConverter::convert)
                .toList();
    }
}
