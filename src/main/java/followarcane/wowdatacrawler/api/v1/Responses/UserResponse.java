package followarcane.wowdatacrawler.api.v1.Responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String username;
    private String email;
    private String role;
}