package cart.persistance.entity;

public class CouponEntity {

    private Long id;
    private String name;
    private String discountType;
    private Double discountValue;

    public CouponEntity(Long id, String name, String discountType, Double discountValue) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountValue = discountValue;
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

    public Double getDiscountValue() {
        return discountValue;
    }
}
