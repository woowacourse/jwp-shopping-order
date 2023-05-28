package cart.domain.order;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;

public class OrderItem {

    private final Long id;
    private final int quantity;
    private final Product product;
    private final Member member;

    public OrderItem(final int quantity, final Product product, final Member member) {
        this(null, quantity, product, member);
    }

    public OrderItem(final Long id, final int quantity, final Product product, final Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public static OrderItem from(CartItem cartItem) {
        return new OrderItem(cartItem.getQuantity(), cartItem.getProduct(), cartItem.getMember());
    }

    public int getPrice() {
        return quantity * product.getPrice();
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Member getMember() {
        return member;
    }
}
