package cart.domain;

import cart.exception.CannotApplyCouponException;

public class FixedDiscountCoupon extends Coupon {

    private final Long id;
    private final Long member_id;
    private final String name;
    private final String imageUrl;
    private final int discountPrice;

    public FixedDiscountCoupon(long member_id, String name, String imageUrl, int discountPrice) {
        this(null, member_id, name, imageUrl, discountPrice);
    }

    public FixedDiscountCoupon(Long id, Long member_id, String name, String imageUrl, int discountPrice) {
        this.id = id;
        this.member_id = member_id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.discountPrice = discountPrice;
    }

    @Override
    public Money apply(Money price) {
        try {
            return Money.from(discountPrice);
        } catch (Exception e) {
            throw new CannotApplyCouponException();
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Money getDiscountPrice() {
        return Money.from(this.discountPrice);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }
}
