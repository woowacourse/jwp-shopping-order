package cart.domain;

import cart.exception.CartItemException;
import cart.exception.ExceptionType;
import java.util.Objects;

public class CartItem {
    private final Long id;
    private final Member member;
    private final Item item;

    public CartItem(Product product, Member member) {
        this(null, member, new Item(product));
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        this(id, member, new Item(product, quantity));
    }

    public CartItem(Long id, Member member, Item item) {
        this.id = id;
        this.member = member;
        this.item = item;
    }

    public void checkOwner(Member member) {
        if (!this.member.equals(member)) {
            throw new CartItemException(ExceptionType.NO_AUTHORITY_CART_ITEM);
        }
    }

    public void changeQuantity(int quantity) {
        item.changeQuantity(quantity);
    }

    public Money calculateCartPrice() {
        return item.calculateItemPrice();
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return item.getProduct();
    }

    public int getQuantity() {
        return item.getQuantity();
    }

    public Item getItem() {
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
