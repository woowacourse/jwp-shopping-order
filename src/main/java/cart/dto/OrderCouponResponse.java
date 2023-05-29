package cart.dto;

import java.util.Objects;

public class OrderCouponResponse {

    private Long id;
    private String name;
    private Integer minPrice;
    private Boolean isAvailable;
    private Integer discountPrice;

    public OrderCouponResponse() {
    }

    public OrderCouponResponse(final Long id, final String name, final Integer minPrice, final Boolean isAvailable, final Integer discountPrice) {
        this.id = id;
        this.name = name;
        this.minPrice = minPrice;
        this.isAvailable = isAvailable;
        this.discountPrice = discountPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderCouponResponse that = (OrderCouponResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(minPrice, that.minPrice)
                && Objects.equals(isAvailable, that.isAvailable)
                && Objects.equals(discountPrice, that.discountPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, minPrice, isAvailable, discountPrice);
    }

    @Override
    public String toString() {
        return "OrderCouponResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minPrice=" + minPrice +
                ", isAvailable=" + isAvailable +
                ", discountPrice=" + discountPrice +
                '}';
    }
}
