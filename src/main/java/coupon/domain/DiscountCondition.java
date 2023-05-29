package coupon.domain;

public interface DiscountCondition {

    boolean isSatisfiedBy(Order order);
}
