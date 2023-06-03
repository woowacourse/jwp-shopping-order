package cart.domain.product;

import cart.domain.Member;
import cart.exception.ItemException;

import java.util.Objects;

public class CartItem extends Item {

    private final Member member;

    public CartItem(Member member, Product product) {
        super(product);

        this.member = member;
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        super(id, quantity, product);

        this.member = member;
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public final void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new ItemException.IllegalMember(this, member);
        }
    }

    public Member getMember() {
        return member;
    }
}
