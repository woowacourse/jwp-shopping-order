package cart.domain;

import cart.exception.CartItemException;
import cart.exception.InvalidOrderCheckedException;
import cart.exception.InvalidOrderProductException;
import cart.exception.InvalidOrderQuantityException;

import java.util.Map;
import java.util.Objects;

public class CartItem {
    private Long id;
    private int quantity;
    private final Product product;
    private final Member member;
    private boolean checked;

    public CartItem(Member member, Product product) {
        this.quantity = 1;
        this.member = member;
        this.product = product;
        this.checked = true;
    }

    public CartItem(Long id, int quantity, Product product, Member member, final boolean checked) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
        this.checked = checked;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void changeChecked(final boolean checked) {
        this.checked = checked;
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

    public boolean isChecked() {
        return checked;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CartItem cartItem = (CartItem) o;
        return id.equals(cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void isSameProductAndQuantity(final Map<Long, Integer> requestProductIdQuantity) {
        final Integer quantity = requestProductIdQuantity.get(product.getId());
        if (Objects.isNull(quantity)) {
            throw new InvalidOrderProductException();
        }
        if (!Objects.equals(quantity, this.quantity)) {
            throw new InvalidOrderQuantityException();
        }
        if (!isChecked()) {
            throw new InvalidOrderCheckedException();
        }
    }
}
