package cart.controller.response;

import cart.domain.Discount;

public class DiscountResponseDto {

    private final Integer discountPrice;
    private final Integer totalPrice;

    private DiscountResponseDto(){
        this(null, null);
    }

    private DiscountResponseDto(final Integer discountPrice, final Integer totalPrice) {
        this.discountPrice = discountPrice;
        this.totalPrice = totalPrice;
    }
    
    public static DiscountResponseDto from(Discount discount) {
        return new DiscountResponseDto(discount.getDiscountPrice(), discount.getPriceAfterDiscount());
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
    
}
