package cart.dto;

public class MemberCouponResponse {
    private final Long id;
    private final String name;
    private final String discountType;
    private final Double discountRate;
    private final Integer discountAmount;

    public MemberCouponResponse(Long id, String name, String discountType, Double discountRate, Integer discountAmount) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
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
}
