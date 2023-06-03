package cart.domain.order;

import cart.domain.cart.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.OrderItemEntity;

public class OrderItem {

    private final Long id;
    private final int quantity;
    private final Product product;
    private final Member member;

    private OrderItem(final int quantity, final Product product, final Member member) {
        this(null, quantity, product, member);
    }

    private OrderItem(final Long id, final int quantity, final Product product, final Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public static OrderItem from(CartItem cartItem) {
        return new OrderItem(cartItem.getQuantity(), cartItem.getProduct(), cartItem.getMember());
    }

    public static OrderItem of(OrderItemEntity orderItemEntity, Product product, Member member) {
        return new OrderItem(orderItemEntity.getId(), orderItemEntity.getQuantity(), product, member);
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
