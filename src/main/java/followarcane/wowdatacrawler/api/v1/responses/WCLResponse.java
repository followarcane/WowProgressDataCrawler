package followarcane.wowdatacrawler.api.v1.responses;

import followarcane.wowdatacrawler.api.v1.responses.model.WCLData;
import lombok.Data;

@Data
public class WCLResponse {
    private WCLData data;
}
