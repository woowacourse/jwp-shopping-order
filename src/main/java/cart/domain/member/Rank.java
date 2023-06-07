package cart.domain.member;

import cart.domain.value.Money;

import java.util.Comparator;
import java.util.stream.Stream;

public enum Rank {

    NORMAL(0, 0),
    SILVER(0.05, 100_000),
    GOLD(0.1, 200_000),
    PLATINUM(0.15, 300_000),
    DIAMOND(0.2, 500_000);

    private final double discountRate;
    private final int minmumCumulativeAmount;

    Rank(double discountRate, int minmumCumulativeAmount) {
        this.discountRate = discountRate;
        this.minmumCumulativeAmount = minmumCumulativeAmount;
    }

    public Money getDiscountPrice(final Money price) {
        return price.multiply(1 - discountRate);
    }

    public static Rank findRank(final Money totalPurchaseAmount) {
        return Stream.of(Rank.values())
                .filter(rank -> rank.minmumCumulativeAmount < totalPurchaseAmount.getMoney())
                .max(Comparator.comparing(Rank::getStandard))
                .orElseThrow();
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public int getStandard() {
        return minmumCumulativeAmount;
    }
}
