package cart.domain;

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
            return new Money(money.value() - discountAmount);
        }

        if (type == CouponType.FIXED_PERCENTAGE) {
            if ((int)(money.value() * discountPercentage) > maxDiscountPrice) {
                return new Money(money.value() - maxDiscountPrice);
            }

            return new Money((int)(money.value() * (1 - discountPercentage)));
        }

        //// TODO: 2023/05/31 커스텀 익셉션 논의 후 설정
        throw new IllegalArgumentException();
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
