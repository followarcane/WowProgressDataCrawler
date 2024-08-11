package followarcane.wowdatacrawler.infrastructure.service;

import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.model.RaiderIOData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RaiderIOServiceTest {

    @InjectMocks
    private RaiderIOService raiderIOService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        Field raiderIOUrlField = RaiderIOService.class.getDeclaredField("raiderIOUrl");
        raiderIOUrlField.setAccessible(true);
        raiderIOUrlField.set(raiderIOService, "https://raider.io/api/v1/characters/profile");
    }

    @Test
    void testFetchRaiderIOData_WithSpecialCharacters() {
        CharacterInfo characterInfo = CharacterInfo.builder()
                .name("Данканчик")
                .region("eu")
                .realm("Гордунни")
                .build();

        RaiderIOData result = raiderIOService.fetchRaiderIOData(characterInfo);

        assertNotNull(result);
        assertEquals("Данканчик", result.getName());
        assertEquals("eu", result.getRegion());
        assertEquals("Gordunni", result.getRealm());
    }
}
