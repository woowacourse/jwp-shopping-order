package cart.domain.cartitem;

import static cart.exception.CartItemException.IllegalMember;

import cart.domain.member.Member;
import cart.domain.product.Product;
import java.util.Objects;

public class CartItem {

    private Long id;
    private Quantity quantity;
    private final Member member;
    private final Product product;

    public CartItem(Product product, Member member) {
        this(null, 1, product, member);
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        this.id = id;
        this.quantity = new Quantity(quantity);
        this.product = product;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity.getValue();
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new IllegalMember(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = new Quantity(quantity);
    }
}
