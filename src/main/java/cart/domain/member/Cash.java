package cart.domain.member;

import java.util.Objects;

public class Cash {

    private final int cash;

    public Cash(final int cash) {
        this.cash = cash;
    }

    public Cash charge(int cashToCharge) {
        int chargedCash = this.cash + cashToCharge;
        return new Cash(chargedCash);
    }

    public boolean isLessThan(int target) {
        return this.cash < target;
    }

    public Cash pay(int cashToPay) {
        return new Cash(this.cash - cashToPay);
    }

    public int getCash() {
        return cash;
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
}
