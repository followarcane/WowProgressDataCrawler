package followarcane.wowdatacrawler.infrastructure.controller;

import followarcane.wowdatacrawler.domain.converter.CharacterInfoResponseConverter;
import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.model.CharacterInfoResponse;
import followarcane.wowdatacrawler.domain.service.CharacterInfoService;
import followarcane.wowdatacrawler.domain.service.WowDataCrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
@RequestMapping("/api/v1/wdc")
public class WowDataCrawlerController {

    private final WowDataCrawlerService wowDataCrawlerService;
    private final CharacterInfoService characterInfoService;
    private final CharacterInfoResponseConverter characterInfoResponseConverter;


    @GetMapping("/latest-lfg")
    @ResponseBody
    public ResponseEntity<List<CharacterInfoResponse>> getLfgs() {
        List<CharacterInfo> characterInfos= characterInfoService.getAllCharacterInfos();
        return ResponseEntity.ok(characterInfoResponseConverter.convert(characterInfos));
    }
}
