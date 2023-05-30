package cart.exception.notfound;

public abstract class NotFoundException extends RuntimeException {

    private final String message;

    public NotFoundException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
