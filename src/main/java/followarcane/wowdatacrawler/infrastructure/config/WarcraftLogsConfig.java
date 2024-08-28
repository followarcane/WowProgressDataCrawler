package followarcane.wowdatacrawler.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "properties.warcraftlogs")
public class WarcraftLogsConfig {
    private String url;
    private String clientId;
    private String clientSecret;
    private String tokenUrl;
}
