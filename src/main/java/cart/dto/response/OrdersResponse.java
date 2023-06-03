package cart.dto.response;

import cart.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersResponse {
    private final Long id;
    private final List<ProductQuantityResponse> orderProducts;
    private final boolean confirmState;

    private OrdersResponse(Long id, List<ProductQuantityResponse> orderProducts, boolean confirmState) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.confirmState = confirmState;
    }

    public static OrdersResponse from(Order order) {
        return new OrdersResponse(
                order.getId(),
                order.getCartProducts().stream().map(ProductQuantityResponse::from).collect(Collectors.toList()),
                order.getConfirmState()
        );
    }

    public Long getId() {
        return id;
    }

    public List<ProductQuantityResponse> getOrderProducts() {
        return orderProducts;
    }

    public boolean isConfirmState() {
        return confirmState;
    }
}
