package cart.domain;

import cart.exception.ErrorStatus;
import cart.exception.ShoppingOrderException;
import java.util.Objects;

public class CartItem {
    private Long id;
    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(Product product, Member member) {
        this(1, product, member);
    }

    public CartItem(Integer quantity, Product product, Member member) {
        this(null, quantity, product, member);
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
            throw new ShoppingOrderException(ErrorStatus.MEMBER_ILLEGAL_ACCESS);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
