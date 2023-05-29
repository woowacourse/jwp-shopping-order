package cart.domain;

import java.util.Map;

public class Order {

    private Long id;
    private final int usedPoint;
    private final Map<Product, Integer> products;
    private final Member member;

    public Order(int usedPoint, Map<Product, Integer> products, Member member) {
        this.usedPoint = usedPoint;
        this.products = products;
        this.member = member;
    }

    public Order(Long id, int usedPoint, Map<Product, Integer> products, Member member) {
        this.id = id;
        this.usedPoint = usedPoint;
        this.products = products;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public Member getMember() {
        return member;
    }
}
