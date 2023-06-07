package cart.coupon.domain;

import java.util.Objects;

public class SpecificCouponStrategy implements CouponStrategy {

    private final Long productId;

    public SpecificCouponStrategy(Long productId) {
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

    public Long getProductId() {
        return productId;
    }
}
