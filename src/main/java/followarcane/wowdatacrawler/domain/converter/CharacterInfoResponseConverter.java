package followarcane.wowdatacrawler.domain.converter;

import followarcane.wowdatacrawler.domain.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CharacterInfoResponseConverter {

    public List<CharacterInfoResponse> convert(List<CharacterInfo> listCharacterInfo) {
        return listCharacterInfo.stream()
                .map(CharacterInfoResponseConverter::convert)
                .collect(Collectors.toList());
    }

    public static CharacterInfoResponse convert(CharacterInfo characterInfo) {
        RaiderIOData raiderIOData = characterInfo.getRaiderIOData();
        RaiderIODataResponse raiderIODataResponse = null;
        if (raiderIOData != null) {
            raiderIODataResponse = convert(raiderIOData);
        }

        List<RaidProgressionResponse> raidProgressions = null;
        if (characterInfo.getRaidProgressions() != null) {
            raidProgressions = characterInfo.getRaidProgressions().stream()
                    .map(CharacterInfoResponseConverter::convert)
                    .collect(Collectors.toList());
        }

        return CharacterInfoResponse.builder()
                .name(characterInfo.getName())
                .guild(Objects.isNull(characterInfo.getGuild()) ? "No Guild" : characterInfo.getGuild())
                .region(characterInfo.getRegion())
                .realm(characterInfo.getRealm())
                .iLevel(characterInfo.getRanking())
                .raiderIODataResponse(raiderIODataResponse)
                .raidProgressions(raidProgressions)
                .build();
    }

    public static RaiderIODataResponse convert(RaiderIOData raiderIOData) {
        return RaiderIODataResponse.builder()
                .classType(raiderIOData.getCharacterClass())
                .race(raiderIOData.getRace())
                .faction(raiderIOData.getFaction())
                .gender(raiderIOData.getGender())
                .thumbnailUrl(raiderIOData.getThumbnailUrl())
                .activeSpecRole(raiderIOData.getActiveSpecRole())
                .achievementPoints(String.valueOf(raiderIOData.getAchievementPoints()))
                .activeSpecName(raiderIOData.getActiveSpecName())
                .build();
    }

    public static RaidProgressionResponse convert(RaidProgression raidProgression) {
        return RaidProgressionResponse.builder()
                .raidName(raidProgression.getRaidName())
                .summary(raidProgression.getSummary())
                .build();
    }
}