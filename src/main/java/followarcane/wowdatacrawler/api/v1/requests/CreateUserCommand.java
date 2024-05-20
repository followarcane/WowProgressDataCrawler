package followarcane.wowdatacrawler.api.v1.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateUserCommand extends UserCommand{
    private String password;
}