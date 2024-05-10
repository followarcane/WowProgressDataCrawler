package followarcane.wowdatacrawler.infrastructure.controller;

import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.service.WowDataCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/v1/wdc")
public class WowDataCrawlerController {

    @Autowired
    private WowDataCrawlerService wowDataCrawlerService;


    @PostMapping("/crawl")
    @ResponseBody
    public List<CharacterInfo> crawl() throws IOException {
        return wowDataCrawlerService.crawlCharacterInfoFromWeb("https://www.wowprogress.com/gearscore/?lfg=1&sortby=ts");
    }
}
