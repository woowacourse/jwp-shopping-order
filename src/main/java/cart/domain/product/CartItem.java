package cart.domain.product;

import cart.domain.Member;
import cart.domain.vo.Quantity;
import cart.exception.CartItemException;
import java.util.Objects;

public class CartItem {

    private final Long id;
    private Quantity quantity;
    private final Product product;
    private final Long memberId;

    public CartItem(final Long id, final Quantity quantity, final Product product, final Long memberId) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.memberId = memberId;
    }

    public CartItem(final Integer quantity, final Member member, final Product product) {
        this(null, new Quantity(quantity), product, member.getId());
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(memberId, member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = new Quantity(quantity);
    }
}
