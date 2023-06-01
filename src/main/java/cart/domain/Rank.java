package cart.domain;

import java.util.Comparator;
import java.util.stream.Stream;

public enum Rank {

    NORMAL(0, 0),
    SILVER(5, 100_000),
    GOLD(10, 200_000),
    PLATINUM(15, 300_000),
    DIAMOND(20, 500_000);

    private final int discountRate;
    private final int standard;

    Rank(int discountRate, int standard) {
        this.discountRate = discountRate;
        this.standard = standard;
    }

    public int getDiscountPrice(final int price) {
        return (int) (price * (1 - (double)discountRate / 100));
    }

    public static Rank findRank(final int totalPurchaseAmount) {
        return Stream.of(Rank.values())
                .filter(rank -> rank.standard < totalPurchaseAmount)
                .max(Comparator.comparing(Rank::getStandard))
                .orElseThrow();
    }

    public int getStandard() {
        return standard;
    }
}
