package cart.dto;

import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderRequest {
    private List<OrderCartItemRequest> products;
    private Long couponId;

    public OrderRequest() {
    }

    public OrderRequest(final List<OrderCartItemRequest> products, final Long couponId) {
        this.products = products;
        this.couponId = couponId;
    }

    public CartItems toCartItems(final Member member) {
        List<CartItem> cartItems = products.stream()
                .map(orderCartItemRequest -> orderCartItemRequest.toCartItem(member))
                .collect(Collectors.toList());
        return new CartItems(cartItems);
    }

    public List<OrderCartItemRequest> getProducts() {
        return products;
    }

    public Long getCouponId() {
        return couponId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderRequest that = (OrderRequest) o;
        return Objects.equals(products, that.products)
                && Objects.equals(couponId, that.couponId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products, couponId);
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "products=" + products +
                ", couponId=" + couponId +
                '}';
    }
}
