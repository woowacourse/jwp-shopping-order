package cart.domain.value;

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

    public int multiply(final double percent) {
        return (int) (money * percent);
    }

    public boolean isSame(final int otherMoney) {
        return this.money == otherMoney;
    }

    public int getMoney() {
        return money;
    }
}
