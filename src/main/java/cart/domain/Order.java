package cart.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private final Long id;
    private final List<Product> products;
    private final Member member;
    private final Point usedPoint;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Order(final Long id, final List<Product> products, final Member member, final Point usedPoint,
                 final LocalDateTime createdAt, final LocalDateTime updatedAt) {
        this.id = id;
        this.products = products;
        this.member = member;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Order(final Long id, final List<Product> products, final Member member, final int usedPoint) {
        this(id, products, member, new Point(usedPoint), null, null);
    }

    public int getTotalPrice() {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }

    public Long getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Member getMember() {
        return member;
    }

    public int getUsedPoint() {
        return usedPoint.getValue();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
