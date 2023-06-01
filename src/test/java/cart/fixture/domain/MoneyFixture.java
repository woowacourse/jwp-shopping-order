package cart.fixture.domain;

import cart.domain.vo.Money;

public abstract class MoneyFixture {

    public static Money 금액(String 금액) {
        return Money.from(금액);
    }

    public static Money 포인트(String 포인트) {
        return Money.from(포인트);
    }
}
