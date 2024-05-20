package followarcane.wowdatacrawler.api.v1.error;


public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User not found");
    }
}
