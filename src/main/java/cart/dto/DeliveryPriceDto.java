package cart.dto;

import cart.domain.Price;

public class DeliveryPriceDto {
    private final int originalPrice;
    private final int discountPrice;

    public DeliveryPriceDto(Price originalPrice, Price discountPrice) {
        this.originalPrice = originalPrice.getValue();
        this.discountPrice = discountPrice.getValue();
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
