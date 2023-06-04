package cart.controller.response;

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
    
    public static DiscountResponseDto of(final Integer originPrice, final Integer priceAfterDiscount) {
        int discountPrice = priceAfterDiscount - originPrice;
        return new DiscountResponseDto(discountPrice, priceAfterDiscount);
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
    
}
