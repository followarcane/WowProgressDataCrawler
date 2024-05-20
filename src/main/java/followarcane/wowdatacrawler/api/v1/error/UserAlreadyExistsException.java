package followarcane.wowdatacrawler.api.v1.error;


public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User already exists");
    }
}
