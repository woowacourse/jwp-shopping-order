package cart.domain;

import java.util.List;

public class Order {
    private final Long id;
    private final Integer price;
    private final Member member;
    private final List<CartItem> products;

    public Order(Long id, Integer price, Member member, List<CartItem> products) {
        this.id = id;
        this.price = price;
        this.member = member;
        this.products = products;
    }

    public Order(Integer price, Member member, List<CartItem> products) {
        this(null, price, member, products);
    }

    public Long getId() {
        return id;
    }

    public Integer getPrice() {
        return price;
    }

    public Member getMember() {
        return member;
    }

    public List<CartItem> getProducts() {
        return products;
    }
}
