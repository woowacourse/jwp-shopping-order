package cart.domain;

import cart.exception.BadRequestException;
import cart.exception.ExceptionType;

public class Coupon {
    private final Long id;
    private final String name;
    private final Integer minOrderPrice;
    private final CouponType type;
    private final Integer discountAmount;
    private final Double discountPercentage;
    private final Integer maxDiscountPrice;

    public Coupon(
        Long id,
        String name,
        Integer minOrderPrice,
        CouponType type,
        Integer discountAmount,
        Double discountPercentage,
        Integer maxDiscountPrice
    ) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.type = type;
        this.discountAmount = discountAmount;
        this.discountPercentage = discountPercentage;
        this.maxDiscountPrice = maxDiscountPrice;
    }

    public Money discount(Money money) {
        if (money.value() < minOrderPrice) {
            return money;
        }

        if (type == CouponType.FIXED_AMOUNT) {
            return Money.subtract(money, new Money(discountAmount));
        }

        if (type == CouponType.FIXED_PERCENTAGE) {
            return getDiscountedMoneyByPercentageCoupon(money);
        }

        throw new BadRequestException(ExceptionType.COUPON_NO_EXIST);
    }

    private Money getDiscountedMoneyByPercentageCoupon(Money money) {
        if (Money.multiply(money, discountPercentage).value() > maxDiscountPrice) {
            return Money.subtract(money, new Money(maxDiscountPrice));
        }
        return Money.multiply(money, 1 - discountPercentage);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getMinOrderPrice() {
        return minOrderPrice;
    }

    public Integer getMaxDiscountPrice() {
        return maxDiscountPrice;
    }

    public CouponType getType() {
        return type;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public boolean isAvailable(Money price) {
        return price.value() >= minOrderPrice;
    }
}
