package cart.domain.vo;

import cart.exception.CashException;
import cart.exception.NotEnoughMoneyToPurchaseException;

import java.util.Objects;

public class Cash {

    private static final int INITIAL_MONEY = 1_000_000_000;
    private static final int MINIMUM_AMOUNT = 0;

    private final int cash;

    public Cash() {
        this.cash = INITIAL_MONEY;
    }

    public Cash(int cash) {
        validate(cash);
        this.cash = cash;
    }

    private void validate(int cash) {
        if (cash < MINIMUM_AMOUNT) {
            throw new CashException.SmallerThanMinimum();
        }
    }

    public Cash consume(Cash userPayment) {
        validateMoneyToUse(userPayment);
        return new Cash(cash - userPayment.cash);
    }

    private void validateMoneyToUse(Cash userPayment) {
        if (userPayment.isGreaterThan(this)) {
            throw new NotEnoughMoneyToPurchaseException();
        }
    }

    public boolean isGreaterThan(Cash availableMoney) {
        return this.cash > availableMoney.cash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cash cash1 = (Cash) o;
        return cash == cash1.cash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cash);
    }

    @Override
    public String toString() {
        return "Cash{" +
                "cash=" + cash +
                '}';
    }

    public int getCash() {
        return cash;
    }
}
