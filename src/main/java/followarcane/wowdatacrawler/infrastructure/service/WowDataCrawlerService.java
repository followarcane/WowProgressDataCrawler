package followarcane.wowdatacrawler.infrastructure.service;

import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.model.RaiderIOData;
import followarcane.wowdatacrawler.domain.model.WarcraftLogsData;
import followarcane.wowdatacrawler.infrastructure.service.cloudflare.FlareSolverrService;
import followarcane.wowdatacrawler.infrastructure.service.repoService.CharacterInfoService;
import followarcane.wowdatacrawler.infrastructure.service.repoService.WarcraftLogsDataService;
import followarcane.wowdatacrawler.infrastructure.service.version.VersionNotifierService;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    private final WarcraftLogsService warcraftLogsService;
    private final WarcraftLogsDataService warcraftLogsDataService;
    private final VersionNotifierService versionNotifier;
    private final FlareSolverrService flareSolverr;

    private List<CharacterInfo> lastFetchedData = new ArrayList<>();

    @Value("${properties.wowprogress.url}")
    private String wowProgressUrl;

    @Value("${properties.wowprogress.limit}")
    private Long wowProgressLimit;

    @Scheduled(fixedRate = 300000)
    public void scheduleFixedRateTask() {
        try {
            List<CharacterInfo> list = crawlWowProgress(wowProgressUrl);
            List<RaiderIOData> raiderIODataList = new ArrayList<>();
            List<WarcraftLogsData> warcraftLogsDataList = new ArrayList<>();
            log.info("Fetched {} player(s)", list.size());

            if (!isFirstElementEqual(list, lastFetchedData)) {
                lastFetchedData = list;
                characterInfoService.deleteAll();
                warcraftLogsDataService.deleteAll();
                raiderIOService.deleteAll();

                String warcraftLogsToken = warcraftLogsService.authenticate().orElse(null);
                if (warcraftLogsToken == null) {
                    String errorMsg = "Failed to authenticate with WarcraftLogs, skipping WarcraftLogs data fetching";
                    log.error(errorMsg);
                    versionNotifier.sendHealthCheck(false, 0, errorMsg);
                    return;
                }

                for (CharacterInfo info : list) {
                    try {
                        RaiderIOData data = raiderIOService.fetchRaiderIOData(info);
                        info.setRaiderIOData(data);

                        warcraftLogsService.fetchCharacterData(warcraftLogsToken, info)
                                .ifPresent(warcraftLogsDataList::add);

                        Pair<String, String> commentaryAndLanguages = fetchCharacterCommentaryAndLanguages(info);
                        info.setCommentary(commentaryAndLanguages.getFirst());
                        info.setLanguages(commentaryAndLanguages.getSecond());

                        raiderIODataList.add(data);
                    } catch (Exception e) {
                        log.error("Failed to fetch and save data for character: " + info.getName(), e);
                    }
                }
                log.info("New data found! Saving {} player(s) to database", list.size());
                characterInfoService.saveAll(list);
                raiderIOService.saveAll(raiderIODataList);
                warcraftLogsService.saveAll(warcraftLogsDataList);
                versionNotifier.sendHealthCheck(true, list.size(),
                        String.format("Successfully fetched and saved data for %d new player(s)", list.size()));
            } else {
                log.info("Data is up to date! Found {} player(s) but no changes detected", list.size());
                versionNotifier.sendHealthCheck(true, list.size(),
                        String.format("Data is up to date (%d player(s) checked)", list.size()));
            }
        } catch (Exception e) {
            String errorMsg = "Crawl task failed: " + e.getMessage();
            log.error(errorMsg);
            versionNotifier.sendHealthCheck(false, 0, errorMsg);
        }
    }

    public List<CharacterInfo> crawlWowProgress(String url) {
        try {
            log.info("Crawling WowProgress through FlareSolverr");

            String html = flareSolverr.getPageContent(url);
            if (html == null) {
                String errorMsg = "Failed to get content from FlareSolverr";
                log.error(errorMsg);
                versionNotifier.sendHealthCheck(false, 0, errorMsg);
                return Collections.emptyList();
            }

            Document doc = Jsoup.parse(html);
            log.info("Page Title: {}", doc.title());
            log.info("HTML Length: {}", html.length());

            if (html.contains("403 Forbidden") || html.contains("Access Denied")) {
                String errorMsg = "Access denied by WowProgress (403)";
                log.error(errorMsg);
                versionNotifier.sendHealthCheck(false, 0, errorMsg);
                return Collections.emptyList();
            }

            // Table'ı kontrol et
            Elements rows = doc.select("table.rating > tbody > tr");
            log.debug("Found {} rows in table", rows.size());

            if (rows.isEmpty()) {
                String errorMsg = "No data found in WowProgress table";
                log.warn(errorMsg);
                log.debug("HTML Content: {}", html);
                versionNotifier.sendHealthCheck(false, 0, errorMsg);
                return Collections.emptyList();
            }

            return parseDocument(doc);
        } catch (Exception e) {
            String errorMsg = "Failed to crawl WowProgress: " + e.getMessage();
            log.error(errorMsg);
            versionNotifier.sendHealthCheck(false, 0, errorMsg);
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

        if (regionFullName.contains("OC")) {
            regionFullName = regionFullName.replace("OC", "US");
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
            String encodedRealm = URLEncoder.encode(characterInfo.getRealm().replace(" ", "-"), StandardCharsets.UTF_8);
            String encodedName = URLEncoder.encode(characterInfo.getName(), StandardCharsets.UTF_8);
            String url = "https://www.wowprogress.com/character/" + characterInfo.getRegion() + "/" + encodedRealm + "/" + encodedName;

            Thread.sleep(2000);

            String html = flareSolverr.getPageContent(url);
            if (html == null) {
                return Pair.of("No Commentary Available", "No Languages Available");
            }

            Document doc = Jsoup.parse(html);

            // Parse the content
            Element commentaryElement = doc.select("div.charCommentary").first();
            Element languagesElement = doc.select("div.language:contains(Languages)").first();

            // Process commentary
            String commentary = (commentaryElement != null) ? convertHtmlToText(commentaryElement.html()) : "No Commentary Available";
            if (commentary.length() > 1997) {
                commentary = commentary.substring(0, 1997) + "..";
            }

            // Process languages
            String languages = (languagesElement != null) ?
                    languagesElement.text().replace("Languages: ", "") :
                    "No Languages Available";

            return Pair.of(commentary, languages);

        } catch (Exception e) {
            log.error("Failed to fetch character commentary and languages for character: " + characterInfo.getName(), e);
            return Pair.of("No Commentary Available", "No Languages Available");
        }
    }

    private String convertHtmlToText(String html) {
        if (html == null) {
            return "";
        }
        return html.replaceAll("<br>", "\n").replaceAll("<[^>]+>", "");
    }

    private boolean isFirstElementEqual(List<CharacterInfo> list1, List<CharacterInfo> list2) {
        if (list1.isEmpty() || list2.isEmpty()) {
            return false;
        }

        return Objects.equals(list1.get(0).getName(), list2.get(0).getName());
    }

    private List<CharacterInfo> parseDocument(Document doc) {
        Elements rows = doc.select("table.rating > tbody > tr");
        return rows.stream()
                .limit(wowProgressLimit + 1)
                .map(this::createCharacterInfoFromRow)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}

