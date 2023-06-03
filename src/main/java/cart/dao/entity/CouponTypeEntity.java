package cart.dao.entity;

public class CouponTypeEntity {

    private final long id;
    private final String name;
    private final String discountType;
    private final long discountAmount;

    public CouponTypeEntity(final long id, final String name, final String discountType, final long discountAmount) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscountType() {
        return discountType;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }
}
