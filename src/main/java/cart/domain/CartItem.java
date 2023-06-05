package cart.domain;

import cart.dto.MemberInfo;
import cart.exception.CartItemException;
import cart.exception.ExceptionType;

public class CartItem {

    private final Long id;
    private final MemberInfo member;
    private final Item item;

    public CartItem(Product product, MemberInfo member) {
        this(null, member, new Item(product));
    }

    public CartItem(Long id, int quantity, Product product, MemberInfo member) {
        this(id, member, new Item(product, quantity));
    }

    public CartItem(Long id, MemberInfo member, Item item) {
        this.id = id;
        this.member = member;
        this.item = item;
    }

    public void checkOwner(MemberInfo member) {
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

    public MemberInfo getMember() {
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
}
