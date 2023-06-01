package cart.domain.product;

import cart.domain.Member;
import cart.domain.vo.Quantity;
import cart.exception.CartItemException;
import java.util.Objects;

public class CartItem {

    private final Long id;
    private Quantity quantity;
    private final Product product;
    private final Member member;

    public CartItem(final Long id, final Quantity quantity, final Product product, final Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public CartItem(final Member member, final Product product) {
        this(null, new Quantity(1), product, member);
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

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = new Quantity(quantity);
    }
}
