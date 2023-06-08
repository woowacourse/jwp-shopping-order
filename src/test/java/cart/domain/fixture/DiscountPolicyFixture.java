package cart.domain.fixture;

import cart.domain.DiscountPolicy;
import cart.domain.Money;
import cart.domain.UpperThresholdPriceDiscountPolicy;

public class DiscountPolicyFixture {
    public static DiscountPolicy defaultDiscountPolicy = new UpperThresholdPriceDiscountPolicy("5만원 이상 구매 시 10% 할인",
            Money.from(50_000), 0.1);

}
