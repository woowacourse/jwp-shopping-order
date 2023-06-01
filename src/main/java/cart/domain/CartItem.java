package cart.domain;

import cart.exception.CartUnauthorizedException;
import java.util.Objects;

public class CartItem {

    private Long id;
    private Quantity quantity;
    private final Product product;
    private final Member member;

    public CartItem(Member member, Product product) {
        this(null, Quantity.DEFAULT, product, member);
    }

    public CartItem(Long id, Quantity quantity, Product product, Member member) {
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

    public Quantity getQuantity() {
        return quantity;
    }

    public int getQuantityCount() {
        return quantity.getCount();
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartUnauthorizedException(id, member.getId());
        }
    }

    public void changeQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public Money getPrice() {
        return product.getPrice();
    }
}
