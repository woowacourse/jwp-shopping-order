package cart.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Coupon {

    private final Long id;
    private final String name;
    private final int discountValue;
    private final int minimumOrderAmount;
    private final LocalDateTime endDate;

    public Coupon(Long id, String name, int discountValue, int minimumOrderAmount, LocalDateTime endDate) {
        this.id = id;
        this.name = name;
        this.discountValue = discountValue;
        this.minimumOrderAmount = minimumOrderAmount;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDiscountValue() {
        return discountValue;
    }

    public int getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coupon coupon = (Coupon) o;
        return discountValue == coupon.discountValue && minimumOrderAmount == coupon.minimumOrderAmount && Objects.equals(id, coupon.id) && Objects.equals(name, coupon.name) && Objects.equals(endDate, coupon.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, discountValue, minimumOrderAmount, endDate);
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", discountValue=" + discountValue +
                ", minimumOrderAmount=" + minimumOrderAmount +
                ", endDate=" + endDate +
                '}';
    }
}
