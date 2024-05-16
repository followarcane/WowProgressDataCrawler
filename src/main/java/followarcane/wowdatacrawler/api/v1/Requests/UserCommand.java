package followarcane.wowdatacrawler.api.v1.Requests;

import lombok.Data;

@Data
public abstract class UserCommand {
    protected String username;
    protected String email;
    protected String role;
}
