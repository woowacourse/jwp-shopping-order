package cart.dto;

import cart.domain.CartItem;

import java.util.List;

public class OrderDto {

    private final Long id;
    private final List<CartItem>  cartItems;
    private final int price;

    public OrderDto(Long id, List<CartItem> cartItems, int price) {
        this.id = id;
        this.cartItems = cartItems;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public int getPrice() {
        return price;
    }
}
