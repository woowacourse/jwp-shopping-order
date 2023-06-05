package cart.domain;

public class Coupon {
    private final Long id;
    private final String name;
    private final CouponType type;
    private final int amount;

    public Coupon(String name, CouponType type, int amount) {
        this(null, name, type, amount);
    }

    public Coupon(Long id, String name, CouponType type, int amount) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type.getText();
    }

    public int getAmount() {
        return amount;
    }

    public int calculateActualPrice(int originalPrice) {
        if (type.equals(CouponType.FIGURE)) {
            return originalPrice - amount;
        }
        return originalPrice * (100 - amount) / 100;
    }
}
