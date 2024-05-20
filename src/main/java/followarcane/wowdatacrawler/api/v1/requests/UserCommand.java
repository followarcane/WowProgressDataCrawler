package followarcane.wowdatacrawler.api.v1.requests;

import followarcane.wowdatacrawler.infrastructure.utils.Authorities;
import lombok.Data;

@Data
public abstract class UserCommand {
    protected String username;
    protected String email;
    protected Authorities role;
}
