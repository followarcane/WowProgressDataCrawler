package followarcane.wowdatacrawler.domain.service;

import followarcane.wowdatacrawler.domain.converter.CharacterInfoResponseConverter;
import followarcane.wowdatacrawler.domain.model.CharacterInfoResponse;
import followarcane.wowdatacrawler.domain.model.CharacterInfo;
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

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
@Transactional
public class WowDataCrawlerService {

    private final CharacterInfoRepository characterInfoRepository;
    private final CharacterInfoResponseConverter characterInfoResponseConverter;

    @Value("${properties.wowprogress.url}")
    private String wowProgressUrl;

    @Scheduled(fixedRate = 30000)
    public void scheduleFixedRateTask() {
        List<CharacterInfoResponse> list = crawlCharacterInfoFromWeb(wowProgressUrl);
        saveToDatabase(characterInfoResponseConverter.convert(list));
    }

    private void saveToDatabase(List<CharacterInfo> list) {
        characterInfoRepository.saveAll(list);
    }

    public List<CharacterInfoResponse> crawlCharacterInfoFromWeb(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
            Elements rows = doc.select("table.rating > tbody > tr");

            List<CharacterInfoResponse> list = new ArrayList<>();
            for (Element row : rows) {
                CharacterInfoResponse characterInfoResponseFromRow = createCharacterInfoFromRow(row);
                list.add(characterInfoResponseFromRow);
            }
            return list;
        } catch (IOException e) {
            log.error("Failed to crawl character info from web", e);
            return Collections.emptyList();
        }
    }

    private CharacterInfoResponse createCharacterInfoFromRow(Element row) {
        String characterName = row.select("td:nth-child(1) > a").text();
        String guildName = row.select("td:nth-child(2) > a").text();
        String realm = row.select("td:nth-child(4) > nobr > a").text();
        String characterScore = row.select("td:nth-child(5)").text();

        log.info("Character: {}, Guild: {}, Realm: {}, Score: {}", characterName, guildName, realm, characterScore);

        return CharacterInfoResponse.builder()
                .name(characterName)
                .guild(guildName)
                .realm(realm)
                .ranking(characterScore)
                .build();
    }
}