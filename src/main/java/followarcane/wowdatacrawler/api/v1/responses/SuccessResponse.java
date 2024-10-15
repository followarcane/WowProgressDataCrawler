package followarcane.wowdatacrawler.api.v1.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SuccessResponse {
    @JsonProperty("access_token")
    private String accessToken;
}
