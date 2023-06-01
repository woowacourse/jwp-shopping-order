package cart.dto.history;

public class OrderedCouponHistory {

    private final long id;
    private final String name;

    public OrderedCouponHistory(final long id, final String name) {
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
