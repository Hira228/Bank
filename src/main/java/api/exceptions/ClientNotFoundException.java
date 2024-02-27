package api.exceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String err) {
        super(err);
    }
}
