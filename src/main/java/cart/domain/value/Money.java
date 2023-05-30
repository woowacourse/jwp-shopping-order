package cart.domain.value;

public class Money {

    private final int money;

    public Money(final int money) {
        validatePrice(Money.this.money);
        this.money = money;
    }

    private void validatePrice(final int money) {
        if (Money.this.money < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
    }

    public int multiply(final double percent) {
        return (int) (money * percent);
    }

    public int getMoney() {
        return money;
    }
}
