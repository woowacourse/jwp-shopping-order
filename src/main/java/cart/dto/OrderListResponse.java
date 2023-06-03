package cart.dto;

import java.util.List;

public class OrderListResponse {

    private Long id;
    private List<OrderProductResponse> products;

    public OrderListResponse() {
    }

    public OrderListResponse(final Long id, final List<OrderProductResponse> products) {
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
