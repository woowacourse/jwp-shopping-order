package cart.dto;

public class CouponDto {

    private final Long id;
    private final String name;
    private final Double discountRate;
    private final Integer discountCharge;

    public CouponDto(final Long id, final String name, final Double discountRate, final Integer discountCharge) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.discountCharge = discountCharge;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public Integer getDiscountCharge() {
        return discountCharge;
    }

}
