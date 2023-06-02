package cart.domain.value;

import java.util.Objects;

public class Money {

    private final int money;

    public Money(final int money) {
        validateMoney(money);
        this.money = money;
    }

    private void validateMoney(final int money) {
        if (money < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
    }

    public Money multiply(final double percent) {
        return new Money((int) (money * percent));
    }

    public Money plus(final Money consumptionAmount) {
        return new Money(money + consumptionAmount.getMoney());
    }

    public Money minus(final Money otherMoney) {
        return new Money(money - otherMoney.getMoney());
    }

    public boolean isOver(final Money otherMoney) {
        return money >= otherMoney.getMoney();
    }

    public boolean isLower(final Money otherMoney) {
        return money < otherMoney.getMoney();
    }

    public int getMoney() {
        return money;
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
