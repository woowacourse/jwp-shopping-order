package cart.domain.coupon;

import static cart.domain.coupon.DiscountPolicyType.NONE;

import cart.domain.VO.Money;

public class Coupon {

    public static final Coupon EMPTY = new Coupon("", NONE, 0L, Money.ZERO, 0L);

    private final Long id;
    private final String name;
    private final DiscountPolicyType discountPolicyType;
    private final Long discountValue;
    private final Money minimumPrice;
    private boolean used;
    private final Long memberId;

    public Coupon(
            final String name,
            final DiscountPolicyType discountPolicyType,
            final Long discountValue,
            final Money minimumPrice
    ) {
        this(null, name, discountPolicyType, discountValue, minimumPrice, false, null);
    }

    public Coupon(
            final String name,
            final DiscountPolicyType discountPolicyType,
            final Long discountValue,
            final Money minimumPrice,
            final Long memberId
    ) {
        this(null, name, discountPolicyType, discountValue, minimumPrice, false, memberId);
    }

    public Coupon(final Long id, final String name, final DiscountPolicyType discountPolicyType,
                  final Long discountValue, final Money minimumPrice,
                  final boolean used, final Long memberId) {
        this.id = id;
        this.name = name;
        this.discountPolicyType = discountPolicyType;
        this.discountValue = discountValue;
        this.minimumPrice = minimumPrice;
        this.used = used;
        this.memberId = memberId;
    }

    public boolean isInvalidPrice(final Money price) {
        return !price.isGreaterThanOrEqual(minimumPrice);
    }

    public Money calculatePrice(final Money price) {
        return discountPolicyType.calculateDiscountValue(discountValue, price);
    }

    public Money calculateDeliveryFee(final Money deliveryFee) {
        return discountPolicyType.calculateDeliveryFee(discountValue, deliveryFee);
    }

    public void use() {
        used = true;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DiscountPolicyType getDiscountPolicyType() {
        return discountPolicyType;
    }

    public Long getDiscountValue() {
        return discountValue;
    }

    public Money getMinimumPrice() {
        return minimumPrice;
    }

    public Long getMinimumPriceValue() {
        return minimumPrice.getLongValue();
    }

    public boolean isUsed() {
        return used;
    }

    public Long getMemberId() {
        return memberId;
    }
}
