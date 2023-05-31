package cart.coupon.domain;

import java.util.Objects;

public class SpecificCouponType implements CouponType {

    private final Long productId;

    public SpecificCouponType(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean canApply(Long productId) {
        return Objects.equals(this.productId, productId);
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.SPECIFIC;
    }
}
