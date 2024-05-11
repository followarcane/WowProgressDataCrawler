package followarcane.wowdatacrawler.domain.service;

import com.nimbusds.jose.shaded.json.JSONObject;
import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.model.RaiderIOData;
import followarcane.wowdatacrawler.domain.repository.CharacterInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
@Transactional
public class WowDataCrawlerService {

    private final CharacterInfoRepository characterInfoRepository;
    private final RaiderIOService raiderIOService;

    private List<CharacterInfo> lastFetchedData = new ArrayList<>();

    @Value("${properties.wowprogress.url}")
    private String wowProgressUrl;

    @Scheduled(fixedRate = 30000)
    public void scheduleFixedRateTask() {
        List<CharacterInfo> list = crawlCharacterInfoFromWeb(wowProgressUrl);

        if (!isFirstElementEqual(list, lastFetchedData)) {
            characterInfoRepository.deleteAll();
            saveToDatabase(list);
            lastFetchedData = list;
            log.info("Last fetched data: {}", lastFetchedData);

            for (CharacterInfo info : lastFetchedData) {
                try {
                    RaiderIOData data = raiderIOService.fetchRaiderIOData(info);
                    raiderIOService.parseAndSaveData(new JSONObject(data.getRaidProgressions()));
                } catch (Exception e) {
                    log.error("Failed to fetch and save RaiderIOData for character: " + info.getName(), e);
                }
            }
        } else {
            log.info("No new data found!");
        }
    }

    private void saveToDatabase(List<CharacterInfo> list) {
        characterInfoRepository.saveAll(list);
    }

    public List<CharacterInfo> crawlCharacterInfoFromWeb(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
            Elements rows = doc.select("table.rating > tbody > tr");

            return rows.stream()
                    .limit(10) // Limit for first 10 elements
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
        String realm = row.select("td:nth-child(4) > nobr > a").text();
        String characterScore = row.select("td:nth-child(5)").text();

        if (characterName.isEmpty()) {
            return null;
        }

        return CharacterInfo.builder()
                .name(characterName)
                .guild(guildName)
                .realm(realm)
                .ranking(characterScore)
                .build();
    }

    private boolean isFirstElementEqual(List<CharacterInfo> list1, List<CharacterInfo> list2) {
        if (list1.isEmpty() || list2.isEmpty()) {
            return false;
        }

        return Objects.equals(list1.get(0).getName(), list2.get(0).getName());
    }
}