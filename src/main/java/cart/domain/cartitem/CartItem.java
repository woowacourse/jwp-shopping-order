package cart.domain.cartitem;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.ForbiddenException;
import java.util.Objects;

public class CartItem {

    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(int quantity, Product product, Member member) {
        this.quantity = quantity;
        this.product = product;
        this.member = member;
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

    public void checkOwner(final Long cartItemId, final String memberName) {
        if (!Objects.equals(this.member.getName(), memberName)) {
            throw new ForbiddenException(String.valueOf(cartItemId), memberName);
        }
    }

    public void changeQuantity(final int quantity) {
        this.quantity = quantity;
    }
}
