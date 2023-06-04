package cart.dto.order;

import cart.domain.Price;

public class DiscountPolicyDetailResponse {
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
}
