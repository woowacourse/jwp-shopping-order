package cart.coupon.domain;

import java.util.Objects;

public class SpecificProductCouponStrategy implements CouponStrategy {

    private final Long productId;

    public SpecificProductCouponStrategy(Long productId) {
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
