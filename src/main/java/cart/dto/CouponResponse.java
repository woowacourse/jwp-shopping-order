package cart.dto;

import cart.domain.Coupon;

public class CouponResponse {

    private final long id;
    private final String type;
    private final int amount;
    private final String name;

    public CouponResponse(long id, String type, int amount, String name) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.name = name;
    }

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getType(),
                coupon.getAmount(),
                coupon.getName()
        );
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }
}
