package followarcane.wowdatacrawler.api.v1.responses.model;

import followarcane.wowdatacrawler.domain.model.WCLRanking;
import lombok.Data;

import java.util.List;

@Data
public class WCLReport {
    private List<WCLRanking> rankings;
}
