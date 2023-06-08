package cart.dto.request;

public class CouponRequest {
    private final String name;
    private final String discountType;
    private final int minimumPrice;
    private final int discountPrice;
    private final int discountRate;

    public CouponRequest(String name, String discountType, int minimumPrice, int discountPrice, int discountRate) {
        this.name = name;
        this.discountType = discountType;
        this.minimumPrice = minimumPrice;
        this.discountPrice = discountPrice;
        this.discountRate = discountRate;
    }

    public String getName() {
        return name;
    }

    public String getDiscountType() {
        return discountType;
    }

    public int getMinimumPrice() {
        return minimumPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
