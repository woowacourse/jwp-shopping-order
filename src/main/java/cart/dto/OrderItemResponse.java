package cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItemResponse {
    private Long id;
    private Integer quantity;
    private ProductResponse product;

    public OrderItemResponse(Long id, Integer quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty("product")
    public ProductResponse getProductResponse() {
        return product;
    }
}
