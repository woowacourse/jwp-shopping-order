package cart.domain;

import java.math.BigDecimal;

public interface DiscountPolicy {

    Money calculateDiscountAmount(Order order);

    boolean canApply(Order order);

    Long getId();

    String getName();

    BigDecimal getDiscountRate();

    Money getThreshold();

}