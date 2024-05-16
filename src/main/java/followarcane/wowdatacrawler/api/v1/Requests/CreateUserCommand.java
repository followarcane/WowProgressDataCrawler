package followarcane.wowdatacrawler.api.v1.Requests;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class CreateUserCommand extends UserCommand{
    private String password;
}