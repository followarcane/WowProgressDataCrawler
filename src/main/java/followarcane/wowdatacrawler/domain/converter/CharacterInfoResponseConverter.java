package followarcane.wowdatacrawler.domain.converter;

import followarcane.wowdatacrawler.domain.model.CharacterInfoResponse;
import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.model.RaiderIOData;
import followarcane.wowdatacrawler.domain.model.RaiderIODataResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class CharacterInfoResponseConverter {

    public List<CharacterInfoResponse> convert(List<CharacterInfo> listCharacterInfo) {
        return listCharacterInfo.stream()
                .map(CharacterInfoResponseConverter::convert)
                .toList();
    }

    public static CharacterInfoResponse convert(CharacterInfo characterInfo) {
        RaiderIOData raiderIOData = characterInfo.getRaiderIOData();
        RaiderIODataResponse raiderIODataResponse = null;
        if (raiderIOData != null) {
            raiderIODataResponse = convert(raiderIOData);
        }

        return CharacterInfoResponse.builder()
                .name(characterInfo.getName())
                .guild(Objects.isNull(characterInfo.getGuild()) ? "No Guild" : characterInfo.getGuild())
                .raid(characterInfo.getRaid())
                .region(characterInfo.getRegion())
                .realm(characterInfo.getRealm())
                .ranking(characterInfo.getRanking())
                .raiderIODataResponse(raiderIODataResponse)
                .build();
    }

    public static RaiderIODataResponse convert(RaiderIOData raiderIOData) {
        return RaiderIODataResponse.builder()
                .classType(raiderIOData.getCharacterClass())
                .race(raiderIOData.getRace())
                .faction(raiderIOData.getFaction())
                .gender(raiderIOData.getGender())
                .build();
    }
}
