package cart.domain.history;

public class CouponHistory {

    private final long id;
    private final String name;

    public CouponHistory(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
