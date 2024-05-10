package followarcane.wowdatacrawler.infrastructure.controller;

import followarcane.wowdatacrawler.domain.converter.CharacterInfoResponseConverter;
import followarcane.wowdatacrawler.domain.model.CharacterInfoResponse;
import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.service.WowDataCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/v1/wdc")
public class WowDataCrawlerController {

    @Autowired
    private WowDataCrawlerService wowDataCrawlerService;


    @PostMapping("/crawl")
    @ResponseBody
    public ResponseEntity<List<CharacterInfo>> crawl() {
        List<CharacterInfoResponse> characterInfoResponseList = wowDataCrawlerService.crawlCharacterInfoFromWeb("https://www.wowprogress.com/gearscore/?lfg=1&sortby=ts");
        List<CharacterInfo> responseDtoList = characterInfoResponseList.stream()
                .map(CharacterInfoResponseConverter::convert)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtoList);
    }
}
