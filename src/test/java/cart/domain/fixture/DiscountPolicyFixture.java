package cart.domain.fixture;

import cart.domain.DefaultDiscountPolicy;
import cart.domain.DiscountPolicy;
import cart.domain.Money;

public class DiscountPolicyFixture {
    public static DiscountPolicy defaultDiscountPolicy = new DefaultDiscountPolicy("5만원 이상 구매 시 10% 할인",
            Money.from(50_000), 0.1);

}
