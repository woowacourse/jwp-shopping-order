package cart.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {
    private final Long id;
    private final LocalDateTime createdAt;
    private final Member member;
    private final int totalPrice;
    private final int finalPrice;

    public Order(final Member member, final int totalPrice, final int finalPrice) {
        this(null, null, member, totalPrice, finalPrice);
    }

    public Order(final Long id, final LocalDateTime createdAt, final Member member, final int totalPrice,
                 final int finalPrice) {
        this.id = id;
        this.createdAt = createdAt;
        this.member = member;
        this.totalPrice = totalPrice;
        this.finalPrice = finalPrice;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Order order = (Order) o;
        return getTotalPrice() == order.getTotalPrice() && getFinalPrice() == order.getFinalPrice()
                && Objects.equals(getId(), order.getId()) && Objects.equals(getCreatedAt(),
                order.getCreatedAt()) && Objects.equals(getMember(), order.getMember());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreatedAt(), getMember(), getTotalPrice(), getFinalPrice());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Member getMember() {
        return member;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getFinalPrice() {
        return finalPrice;
    }
}
