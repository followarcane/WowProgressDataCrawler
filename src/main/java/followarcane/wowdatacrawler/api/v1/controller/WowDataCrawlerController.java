package followarcane.wowdatacrawler.api.v1.controller;

import followarcane.wowdatacrawler.domain.converter.ResponseConverter;
import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.api.v1.responses.CharacterInfoResponse;
import followarcane.wowdatacrawler.infrastructure.service.CharacterInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
@RequestMapping("/api/v1/wdc")
public class WowDataCrawlerController {

    private final CharacterInfoService characterInfoService;
    private final ResponseConverter responseConverter;


    @GetMapping("/latest-lfg")
    public ResponseEntity<List<CharacterInfoResponse>> getLfgs() {
        List<CharacterInfo> characterInfos = characterInfoService.getAllCharacterInfos();
        return ResponseEntity.ok(responseConverter.convert(characterInfos));
    }
}
