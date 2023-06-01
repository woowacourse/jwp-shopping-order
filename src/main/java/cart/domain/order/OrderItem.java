package cart.domain.order;

import cart.domain.product.Product;
import cart.domain.vo.Money;
import cart.domain.vo.Quantity;

import java.util.Objects;

public class OrderItem {

    private final Long id;
    private final Product product;
    private final Quantity quantity;
    private final Long memberId;

    public OrderItem(Product product, Quantity quantity, Long memberId) {
        this(null, product, quantity, memberId);
    }

    public OrderItem(Long id, Product product, Quantity quantity, Long memberId) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.memberId = memberId;
    }

    public Money totalPrice() {
        return product.getPrice().times(quantity);
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Long getMemberId() {
        return memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(product, orderItem.product) && Objects.equals(quantity, orderItem.quantity) && Objects.equals(memberId, orderItem.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, memberId);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                ", memberId=" + memberId +
                '}';
    }
}
