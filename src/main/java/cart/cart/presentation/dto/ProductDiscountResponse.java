package cart.cart.presentation.dto;

public class ProductDiscountResponse {
    private long productId;
    private long originalPrice;
    private long discountPrice;

    public ProductDiscountResponse() {
    }

    public ProductDiscountResponse(long productId, long originalPrice, long discountPrice) {
        this.productId = productId;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
    }

    public long getProductId() {
        return productId;
    }

    public long getOriginalPrice() {
        return originalPrice;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }
}
