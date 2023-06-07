package cart.domain.cartitem;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.domain.vo.Money;
import cart.domain.vo.Quantity;
import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {

    private static final int MINIMUM_QUANTITY = 0;

    private final Long id;
    private final Product product;
    private final Member member;
    private Quantity quantity;

    public CartItem(Product product, Member member) {
        this(null, product, member, MINIMUM_QUANTITY);
    }

    public CartItem(Product product, Member member, int quantity) {
        this(null, product, member, quantity);
    }

    public CartItem(Long id, Product product, Member member, int quantity) {
        validate(quantity, product);
        this.id = id;
        this.quantity = Quantity.from(quantity);
        this.product = product;
        this.member = member;
    }

    private void validate(int quantity, Product product) {
        if (quantity < MINIMUM_QUANTITY) {
            throw new CartItemException.InvalidQuantity(MINIMUM_QUANTITY);
        }
        if (Objects.isNull(product)) {
            throw new CartItemException.InvalidProduct();
        }
    }

    public boolean isSameProduct(CartItem other) {
        return product.equals(other.product);
    }

    public CartItem mergePlus(CartItem other) {
        if (!isSameProduct(other)) {
            throw new CartItemException.MergeCartItem();
        }
        return new CartItem(product, member, quantity.increase(other.quantity).getValue());
    }

    public CartItem mergeMinus(CartItem other) {
        if (!isSameProduct(other)) {
            throw new CartItemException.MergeCartItem();
        }
        return new CartItem(product, member, quantity.decrease(other.quantity).getValue());
    }

    public CartItem changeQuantity(Quantity otherQuantity) {
        this.quantity = otherQuantity;
        return this;
    }

    public Money totalPrice() {
        return product.getPrice().times(quantity);
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.InvalidMember();
        }
    }

    public boolean isQuantityZero() {
        return quantity.isZero();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(product, cartItem.product) && Objects.equals(member, cartItem.member) && Objects.equals(quantity, cartItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, member, quantity);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", product=" + product +
                ", member=" + member +
                ", quantity=" + quantity +
                '}';
    }
}
