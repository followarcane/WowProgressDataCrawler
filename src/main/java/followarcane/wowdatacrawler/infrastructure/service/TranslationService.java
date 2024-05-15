package followarcane.wowdatacrawler.infrastructure.service;

import com.google.cloud.translate.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    private final Translate translate;

    @Value("${properties.google.api.key}")
    private String apiKey;

    public TranslationService() {
        translate = TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();
    }

    public String translateText(String originalText, String targetLanguage) {
        Translation translation = translate.translate(
                originalText,
                Translate.TranslateOption.targetLanguage(targetLanguage),
                Translate.TranslateOption.model("base")
        );
        return translation.getTranslatedText();
    }
}