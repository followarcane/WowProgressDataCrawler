package followarcane.wowdatacrawler.domain.service;

import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class WowDataCrawlerService {

    public List<CharacterInfo> crawlCharacterInfoFromWeb(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
            Elements rows = doc.select("table.rating > tbody > tr");

            List<CharacterInfo> list = new ArrayList<>();
            for (Element row : rows) {
                CharacterInfo characterInfoFromRow = createCharacterInfoFromRow(row);
                list.add(characterInfoFromRow);
            }
            return list;
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

        log.info("Character: {}, Guild: {}, Realm: {}, Score: {}", characterName, guildName.isEmpty() ? "None" : guildName, realm, characterScore);

        return CharacterInfo.builder()
                .name(characterName)
                .guild(guildName)
                .realm(realm)
                .ranking(characterScore)
                .build();
    }
}