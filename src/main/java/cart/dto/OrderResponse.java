package cart.dto;

import java.util.Date;
import java.util.List;

public class OrderResponse {

    private final Long id;
    private final List<OrderItemResponse> cartItems;
    private final Date date;
    private final int price;

    public OrderResponse(final Long id, final List<OrderItemResponse> cartItems, final Date date, final int price) {
        this.id = id;
        this.cartItems = cartItems;
        this.date = date;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemResponse> getCartItems() {
        return cartItems;
    }

    public Date getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }
}
