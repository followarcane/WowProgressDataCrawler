package followarcane.wowdatacrawler.infrastructure.service;

import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.model.RaiderIOData;
import followarcane.wowdatacrawler.infrastructure.utils.Regions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class WowDataCrawlerService {

    private final CharacterInfoService characterInfoService;
    private final RaiderIOService raiderIOService;

    private List<CharacterInfo> lastFetchedData = new ArrayList<>();

    @Value("${properties.wowprogress.url}")
    private String wowProgressUrl;

    @Value("${properties.wowprogress.limit}")
    private Long wowProgressLimit;

    @Scheduled(fixedRate = 30000)
    public void scheduleFixedRateTask() {
        List<CharacterInfo> list = crawlWowProgress(wowProgressUrl);
        List<RaiderIOData> raiderIODataList = new ArrayList<>();

        if (!isFirstElementEqual(list, lastFetchedData)) {
            lastFetchedData = list;
            characterInfoService.deleteAll();
            raiderIOService.deleteAll();

            for (CharacterInfo info : list) {
                try {
                    RaiderIOData data = raiderIOService.fetchRaiderIOData(info);
                    info.setRaiderIOData(data);

                    Pair<String, String> commentaryAndLanguages = fetchCharacterCommentaryAndLanguages(info);
                    info.setCommentary(commentaryAndLanguages.getFirst());
                    info.setLanguages(commentaryAndLanguages.getSecond());

                    raiderIODataList.add(data);
                } catch (Exception e) {
                    log.error("Failed to fetch and save RaiderIOData for character: " + info.getName(), e);
                }
            }
            log.info("New data found! Saving to database...");
            characterInfoService.saveAll(list);
            raiderIOService.saveAll(raiderIODataList);
        } else {
            log.info("No new data found!");
        }
    }


    public List<CharacterInfo> crawlWowProgress(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
            Elements rows = doc.select("table.rating > tbody > tr");

            return rows.stream()
                    .limit(wowProgressLimit + 1)
                    .map(this::createCharacterInfoFromRow)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Failed to crawl character info from web", e);
            return Collections.emptyList();
        }
    }

    private CharacterInfo createCharacterInfoFromRow(Element row) {
        String characterName = row.select("td:nth-child(1) > a").text();
        String guildName = row.select("td:nth-child(2) > a").text();
        String regionFullName = row.select("td:nth-child(4) > nobr > a").text();
        String characterScore = row.select("td:nth-child(5)").text();

        if (characterName.isEmpty() || regionFullName.isEmpty()) {
            return null;
        }

        String[] splitRegion = regionFullName.split("-");
        String regionName = splitRegion[0].trim().split(" ")[0];
        Regions region = Regions.valueOf(regionName.toUpperCase());
        String realm = splitRegion[1].trim();

        return CharacterInfo.builder()
                .name(characterName)
                .guild(guildName)
                .region(region.name())
                .realm(realm)
                .ranking(characterScore)
                .isRussian(regionFullName.contains("(RU)"))
                .build();
    }

    public Pair<String, String> fetchCharacterCommentaryAndLanguages(CharacterInfo characterInfo) {
        try {
            String url = "https://www.wowprogress.com/character/" + characterInfo.getRegion() + "/" + characterInfo.getRealm().replace(" ","-") + "/" + characterInfo.getName();
            Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
            Element commentaryElement = doc.select("div.charCommentary").first();
            Element languagesElement = doc.select("div.language:contains(Languages)").first();

            String commentary = (commentaryElement != null) ? commentaryElement.text() : "No Commentary Available";
            if (commentary.length() > 1997) {
                commentary = commentary.substring(0, 1997) + "..";
            }
            String languages = (languagesElement != null) ? languagesElement.text().replace("Languages: ", "") : "No Languages Available";

            return Pair.of(commentary, languages);
        } catch (IOException e) {
            log.error("Failed to fetch character commentary and languages for character: " + characterInfo.getName());
            return Pair.of("No Commentary Available", "No Languages Available");
        }
    }

    private boolean isFirstElementEqual(List<CharacterInfo> list1, List<CharacterInfo> list2) {
        if (list1.isEmpty() || list2.isEmpty()) {
            return false;
        }

        return Objects.equals(list1.get(0).getName(), list2.get(0).getName());
    }
}