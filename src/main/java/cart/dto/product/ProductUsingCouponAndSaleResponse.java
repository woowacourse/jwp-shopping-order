package cart.dto.product;

public class ProductUsingCouponAndSaleResponse {

    private final long productId;
    private final int originalPrice;
    private final int discountPrice;

    public ProductUsingCouponAndSaleResponse(final long productId, final int originalPrice, final int discountPrice) {
        this.productId = productId;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
    }
    
    public long getProductId() {
        return productId;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
