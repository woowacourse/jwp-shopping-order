package cart.dto;

public class CouponResponse {
    private final Long id;
    private final String name;
    private final String discountType;
    private final Double discountRate;
    private final Integer discountAmount;
    private final Integer minimumPrice;

    public CouponResponse(Long id, String name, String discountType, Double discountRate, Integer discountAmount, Integer minimumPrice) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.minimumPrice = minimumPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscountType() {
        return discountType;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Integer getMinimumPrice() {
        return minimumPrice;
    }
}
/*
                                "id" : 1,
                                            "name" : "5월의 달 20% 할인 쿠폰",
                                            "discountType" : "percentage",
                                            "discountRate" : 0.2,
                                            "discountAmount" : 0,
                                            "minimumPrice" : 50000
 */
