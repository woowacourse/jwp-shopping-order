package cart.coupon;

public class Coupon {
    private final long id;
    private final long memberId;
    private final String name;

    public Coupon(long id, long memberId, String name) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getMemberId() {
        return memberId;
    }
}
