package cart.exception.notfound;

public class NotFoundException extends RuntimeException {
    protected final Long id;
    protected final String name;

    public NotFoundException(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
