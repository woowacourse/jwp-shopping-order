package cart.domain;

public class Coupon {

    private final Long id;
    private final String name;
    private final String description;
    private final int discountAmount;
    private final boolean usageStatus;

    public Coupon(final Long id,
                  final String name,
                  final String description,
                  final int discountAmount,
                  final boolean usageStatus
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.discountAmount = discountAmount;
        this.usageStatus = usageStatus;
    }

    public static Coupon createCouponType(
            final Long id,
            final String name,
            final String description,
            final int discountAmount
    ) {
        return new Coupon(id, name, description, discountAmount, true);
    }

    public boolean isNotUsed() {
        return !usageStatus;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public boolean isUsageStatus() {
        return usageStatus;
    }
}
