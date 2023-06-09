package cart.service.response;

import cart.domain.vo.Price;

public class DiscountPriceResponse {

    private final Integer discountPrice;
    private final Integer totalPrice;

    private DiscountPriceResponse() {
        this(null, null);
    }

    public DiscountPriceResponse(final Integer discountPrice, final Integer totalPrice) {
        this.discountPrice = discountPrice;
        this.totalPrice = totalPrice;
    }

    public static DiscountPriceResponse of(final Price discountPrice, final Price totalPrice) {
        return new DiscountPriceResponse(discountPrice.getValue() * -1, totalPrice.getValue());
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
}
