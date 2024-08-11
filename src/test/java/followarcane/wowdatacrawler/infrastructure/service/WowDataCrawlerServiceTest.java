package followarcane.wowdatacrawler.infrastructure.service;

import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.util.Pair;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WowDataCrawlerServiceTest {

    @InjectMocks
    private WowDataCrawlerService wowDataCrawlerService;

    @InjectMocks
    private RaiderIOService raiderIOService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchCharacterData_WithSpecialCharacters() {
        CharacterInfo characterInfo = CharacterInfo.builder()
                .name("Триндллок")
                .region("eu")
                .realm("ревущий-фьорд")
                .build();

        Pair<String, String> commentaryAndLanguages = wowDataCrawlerService.fetchCharacterCommentaryAndLanguages(characterInfo);
        assertNotNull(commentaryAndLanguages);
        assertFalse(commentaryAndLanguages.getFirst().isEmpty());
        assertFalse(commentaryAndLanguages.getSecond().isEmpty());
    }
}
