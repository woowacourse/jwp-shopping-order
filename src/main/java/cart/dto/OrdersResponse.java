package cart.dto;

import cart.domain.CartItem;

import java.util.List;

public class OrdersResponse {
    private Long id;
    private List<CartItem> ordersProduct;
    private boolean confirmState;
    private OrdersResponse(){

    }

    public OrdersResponse(Long id, List<CartItem> ordersProduct, boolean confirmState) {
        this.id = id;
        this.ordersProduct = ordersProduct;
        this.confirmState = confirmState;
    }

    public Long getId() {
        return id;
    }

    public List<CartItem> getOrdersProduct() {
        return ordersProduct;
    }

    public boolean isConfirmState() {
        return confirmState;
    }
}
