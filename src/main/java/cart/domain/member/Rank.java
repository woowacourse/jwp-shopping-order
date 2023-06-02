package cart.domain.member;

import cart.domain.value.Money;

import java.util.Comparator;
import java.util.stream.Stream;

public enum Rank {

    NORMAL(0, 0, "일반"),
    SILVER(5, 100_000, "실버"),
    GOLD(10, 200_000, "골드"),
    PLATINUM(15, 300_000, "플래티넘"),
    DIAMOND(20, 500_000, "다이아몬드");

    private final int discountRate;
    private final int standard;
    private final String korean;

    Rank(int discountRate, int standard, String korean) {
        this.discountRate = discountRate;
        this.standard = standard;
        this.korean = korean;
    }

    public Money getDiscountPrice(final Money price) {
        return (price.multiply((1 - (double) discountRate / 100)));
    }

    public static Rank findRank(final Money totalPurchaseAmount) {
        return Stream.of(Rank.values())
                .filter(rank -> rank.standard < totalPurchaseAmount.getMoney())
                .max(Comparator.comparing(Rank::getStandard))
                .orElseThrow();
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int getStandard() {
        return standard;
    }

    public String getKorean() {
        return korean;
    }
}
