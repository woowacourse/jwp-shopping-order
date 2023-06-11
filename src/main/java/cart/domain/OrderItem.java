package cart.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OrderItem {

    private final Long id;
    private final Product orderedProduct;
    private final int quantity;
    private final List<MemberCoupon> memberCoupons;

    public OrderItem(CartItem cartItem) {
        this(
                null,
                cartItem.getProduct(),
                cartItem.getQuantity(),
                cartItem.getCoupons()
        );
    }

    public OrderItem(Long id, Product orderedProduct, int quantity) {
        this(id, orderedProduct, quantity, Collections.emptyList());
    }

    public OrderItem(Long id, Product orderedProduct, int quantity, List<MemberCoupon> memberCoupons) {
        this.id = id;
        this.orderedProduct = orderedProduct;
        this.quantity = quantity;
        this.memberCoupons = new ArrayList<>(memberCoupons);
    }

    public Long getId() {
        return id;
    }

    public Product getOrderedProduct() {
        return orderedProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getTotal() {
        Money price = orderedProduct.getPrice().multiply(quantity);
        for (MemberCoupon memberCoupon : memberCoupons) {
            price = memberCoupon.getDiscounted(price);
        }
        return price;
    }

    public List<MemberCoupon> getUsedCoupons() {
        return memberCoupons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        OrderItem orderItem = (OrderItem)o;

        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderedProduct=" + orderedProduct +
                ", quantity=" + quantity +
                ", memberCoupons=" + memberCoupons +
                '}';
    }
}
