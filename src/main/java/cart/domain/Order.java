package cart.domain;

import java.util.List;

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
}
