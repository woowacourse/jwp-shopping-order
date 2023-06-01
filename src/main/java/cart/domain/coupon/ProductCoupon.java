package cart.domain.coupon;

import cart.domain.Price;
import cart.domain.policy.DiscountPolicy;

import java.util.Objects;

public class ProductCoupon implements Coupon {
    public static final String CATEGORY = "all";
    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;


    public ProductCoupon(Long id, String name, DiscountPolicy discountPolicy) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
    }

    public ProductCoupon(String name, DiscountPolicy discountPolicy) {
        this(null, name, discountPolicy);
    }

    @Override
    public Price apply(Price price) {
        return discountPolicy.discount(price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCoupon that = (ProductCoupon) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDiscountPolicyName() {
        return discountPolicy.getName();
    }

    @Override
    public int getDiscountValue() {
        return discountPolicy.getValue();
    }

    @Override
    public String getCategory() {
        return CATEGORY;
    }
}
