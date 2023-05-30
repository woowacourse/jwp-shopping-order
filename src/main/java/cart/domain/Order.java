package cart.domain;

import java.util.List;
import java.util.Objects;

public class Order {

    private Long id;
    private int savedPoint;
    private final int usedPoint;
    private final List<OrderItem> productCounts;
    private final Member member;

    public Order(int usedPoint, List<OrderItem> productCounts, Member member) {
        this.usedPoint = usedPoint;
        this.productCounts = productCounts;
        this.member = member;
    }

    public Order(Long id, int savedPoint, int usedPoint, List<OrderItem> productCounts, Member member) {
        this.id = id;
        this.savedPoint = savedPoint;
        this.usedPoint = usedPoint;
        this.productCounts = productCounts;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public int getSavedPoint() {
        return savedPoint;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public List<OrderItem> getProductCounts() {
        return productCounts;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return savedPoint == order.savedPoint && usedPoint == order.usedPoint && Objects.equals(id, order.id) && Objects.equals(productCounts, order.productCounts) && Objects.equals(member, order.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, savedPoint, usedPoint, productCounts, member);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", savedPoint=" + savedPoint +
                ", usedPoint=" + usedPoint +
                ", productCounts=" + productCounts +
                ", member=" + member +
                '}';
    }
}
