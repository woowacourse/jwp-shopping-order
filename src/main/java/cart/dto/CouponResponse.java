package cart.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class CouponResponse {
    private Long id;
    private String name;
    private Integer minOrderPrice;
    private Integer maxDiscountPrice;
    private String type;
    private Integer discountAmount;
    private Double discountPercentage;

    public CouponResponse() {
    }

    public CouponResponse(final Long id, final String name, final Integer minOrderPrice, final Integer maxDiscountPrice, final String type, final Integer discountAmount, final Double discountPercentage) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.maxDiscountPrice = maxDiscountPrice;
        this.type = type;
        this.discountAmount = discountAmount;
        this.discountPercentage = discountPercentage;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getMinOrderPrice() {
        return minOrderPrice;
    }

    public Integer getMaxDiscountPrice() {
        return maxDiscountPrice;
    }

    public String getType() {
        return type;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CouponResponse that = (CouponResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(minOrderPrice, that.minOrderPrice)
                && Objects.equals(maxDiscountPrice, that.maxDiscountPrice)
                && Objects.equals(type, that.type)
                && Objects.equals(discountAmount, that.discountAmount)
                && Objects.equals(discountPercentage, that.discountPercentage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, minOrderPrice, maxDiscountPrice, type, discountAmount, discountPercentage);
    }

    @Override
    public String toString() {
        return "CouponResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minOrderPrice=" + minOrderPrice +
                ", maxDiscountPrice=" + maxDiscountPrice +
                ", type='" + type + '\'' +
                ", discountAmount=" + discountAmount +
                ", discountPercentage=" + discountPercentage +
                '}';
    }
}
