package cart.domain;

import java.util.Objects;

import cart.exception.ForbiddenException;
import cart.exception.ExceptionType;

public class CartItem {
    private final Product product;
    private final Member member;
    private Long id;
    private int quantity;

    public CartItem(Member member, Product product) {
        this.quantity = 1;
        this.member = member;
        this.product = product;
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new ForbiddenException(ExceptionType.FORBIDDEN);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderItem toOrderItem() {
        return new OrderItem(null, null, product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), this.quantity);
    }
}
