package cart.domain;

public class Coupon {
    private final Long id;
    private final String name;
    private final CouponType type;
    private final int figure;

    public Coupon(Long id, String name, CouponType type, int figure) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.figure = figure;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CouponType getType() {
        return type;
    }

    public int getFigure() {
        return figure;
    }

    public int calculateActualPrice(int originalPrice) {
        if (type.equals(CouponType.AMOUNT)) {
            return originalPrice - figure;
        }
        return originalPrice * (100 - figure) / 100;
    }
}
