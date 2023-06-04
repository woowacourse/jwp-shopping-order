package cart.dto;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;

import java.util.Objects;

public class OrderCartItemRequest {
    private Long cartItemId;
    private Integer quantity;
    private String name;
    private Integer price;
    private String imageUrl;

    public OrderCartItemRequest() {
    }

    public OrderCartItemRequest(final Long cartItemId, final Integer quantity, final String name, final Integer price, final String imageUrl) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public CartItem toCartItem(final Member member) {
        return new CartItem(
                cartItemId,
                quantity,
                new Product(null, name, price, imageUrl),
                member
        );
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderCartItemRequest that = (OrderCartItemRequest) o;
        return Objects.equals(cartItemId, that.cartItemId)
                && Objects.equals(quantity, that.quantity)
                && Objects.equals(name, that.name)
                && Objects.equals(price, that.price)
                && Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartItemId, quantity, name, price, imageUrl);
    }

    @Override
    public String toString() {
        return "OrderCartRequest{" +
                "cartItemId=" + cartItemId +
                ", quantity=" + quantity +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
