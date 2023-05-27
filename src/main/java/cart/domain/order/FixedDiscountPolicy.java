package cart.domain.order;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum FixedDiscountPolicy implements DiscountPolicy{

    NO_DISCOUNT(0, 50_000, 0),
    SMALL_DISCOUNT(50_000, 100_000, 2_000),
    MIDDLE_DISCOUNT(100_000, 200_000, 5_000),
    LARGE_DISCOUNT(200_000, Integer.MAX_VALUE, 12_000);

    private final int inclusiveMin;
    private final int exclusiveMax;
    private final int discountPrice;

    FixedDiscountPolicy(final int inclusiveMin, final int inclusiveMax, final int discountPrice) {
        this.inclusiveMin = inclusiveMin;
        this.exclusiveMax = inclusiveMax;
        this.discountPrice = discountPrice;
    }

    public static FixedDiscountPolicy from(final Price price) {
        return Arrays.stream(values())
                .filter((discountPolicy) -> inRangeOf(discountPolicy, price))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 금액에 적용할 수 있는 할인 제도가 존재하지 않습니다."));
    }

    private static boolean inRangeOf(final FixedDiscountPolicy discountPolicy, final Price price) {
        final int value = price.getValue();
        return value >= discountPolicy.inclusiveMin && value < discountPolicy.exclusiveMax;
    }

    @Override
    public Price discount(final Price price) {
        return price.minus(discountPrice);
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
