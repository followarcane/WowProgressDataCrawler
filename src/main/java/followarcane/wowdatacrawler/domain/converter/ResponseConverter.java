package followarcane.wowdatacrawler.domain.converter;

import followarcane.wowdatacrawler.api.v1.responses.*;
import followarcane.wowdatacrawler.domain.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ResponseConverter {

    public List<CharacterInfoResponse> convert(List<CharacterInfo> listCharacterInfo) {
        return listCharacterInfo.stream()
                .map(ResponseConverter::convert)
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
                    .map(ResponseConverter::convert)
                    .collect(Collectors.toList());
        }

        List<BossRankResponse> bossRanks = null;
        if (characterInfo.getBossRanks() != null) {
            bossRanks = characterInfo.getBossRanks().stream()
                    .map(ResponseConverter::convert)
                    .collect(Collectors.toList());
        }

        WarcraftLogsDataResponse warcraftLogsDataResponse = null;
        if (characterInfo.getWarcraftLogsData() != null) {
            warcraftLogsDataResponse = convert(characterInfo.getWarcraftLogsData());
        }

        return CharacterInfoResponse.builder()
                .name(characterInfo.getName())
                .guild(Objects.isNull(characterInfo.getGuild()) ? "No Guild" : characterInfo.getGuild())
                .region(characterInfo.getRegion())
                .realm(characterInfo.getRealm())
                .iLevel(characterInfo.getRanking())
                .raiderIOData(raiderIODataResponse)
                .raidProgressions(raidProgressions)
                .commentary(characterInfo.getCommentary())
                .languages(characterInfo.getLanguages())
                .bossRanks(bossRanks)
                .warcraftLogsData(warcraftLogsDataResponse)
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

    public static BossRankResponse convert(BossRank bossRank) {
        return BossRankResponse.builder()
                .encounterName(bossRank.getEncounterName())
                .rankPercent(bossRank.getRankPercent())
                .build();
    }

    public static WarcraftLogsDataResponse convert(WarcraftLogsData warcraftLogsData) {
        return WarcraftLogsDataResponse.builder()
                .zoneName(warcraftLogsData.getZoneName())
                .metric(warcraftLogsData.getMetric())
                .difficulty(warcraftLogsData.getDifficulty())
                .bestPerformanceAverage(warcraftLogsData.getBestPerformanceAverage())
                .build();
    }

    public UserResponse convertUser(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public List<UserResponse> convertUser(List<User> listUser) {
        return listUser.stream()
                .map(ResponseConverter::convert)
                .collect(Collectors.toList());
    }

    public static UserResponse convert(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
