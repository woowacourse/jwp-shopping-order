package cart.domain;

import cart.exception.IllegalAccessCartException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.util.Objects;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class CartItem {

    private final Long id;
    private final Member member;
    private final Product product;
    private final int quantity;

    public CartItem(final Member member, final Product product, final int quantity) {
        this(null, member, product, quantity);
    }

    public void validateOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new IllegalAccessCartException(this, member);
        }
    }

    public CartItem changeQuantity(final int quantity) {
        return new CartItem(id, member, product, quantity);
    }
}
