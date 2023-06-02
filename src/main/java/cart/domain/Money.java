package cart.domain;

import java.util.Objects;

public class Money {
    private final int money;

    private Money(int money) {
        validate(money);
        this.money = money;
    }

    private void validate(int money) {
        if (money < 0) {
            throw new IllegalArgumentException("돈은 0 이상의 정수이어야 합니다.");
        }
    }

    public static Money from(int money) {
        return new Money(money);
    }

    public Money plus(Money other) {
        return new Money(other.money + this.money);
    }

    public Money multiply(int quantity) {
        return new Money(this.money * quantity);
    }

    public Money minus(Money other) {
        return new Money(this.money - other.money);
    }

    public boolean isLargerThan(Money money) {
        return this.money > money.money;
    }

    public int toInt() {
        return this.money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money1 = (Money) o;
        return money == money1.money;
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }

}
