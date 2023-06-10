package cart.dto.order;

import cart.domain.Price;

public class DiscountPolicyDetailResponse implements Comparable<DiscountPolicyDetailResponse> {
    private final int minimumPrice;
    private final int discountPrice;

    private DiscountPolicyDetailResponse(final int minimumPrice, final int discountPrice) {
        this.minimumPrice = minimumPrice;
        this.discountPrice = discountPrice;
    }

    public static DiscountPolicyDetailResponse of(final Price minimumPrice, final Price discountPrice) {
        return new DiscountPolicyDetailResponse(minimumPrice.getValue(), discountPrice.getValue());
    }

    public int getMinimumPrice() {
        return minimumPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    @Override
    public int compareTo(final DiscountPolicyDetailResponse o) {
        return Integer.compare(this.minimumPrice, o.minimumPrice);
    }
}
