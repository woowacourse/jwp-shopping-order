package cart.dto.response;

import java.util.List;

public class OrderResponse {

    private final Long id;
    private final List<OrderProductResponse> products;

    public OrderResponse(final Long id, final List<OrderProductResponse> products) {
        this.id = id;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public List<OrderProductResponse> getProducts() {
        return products;
    }
}
