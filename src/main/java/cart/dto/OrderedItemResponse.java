package cart.dto;

import cart.domain.OrderedItem;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderedItemResponse {
    private final Long id;
    private final Integer quantity;
    private final ProductResponse product;

    public OrderedItemResponse(Long id, Integer quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static OrderedItemResponse of(OrderedItem orderedItem) {
        ProductResponse productResponse = ProductResponse.of(orderedItem.getProduct());
        return new OrderedItemResponse(orderedItem.getId(), orderedItem.getQuantity(), productResponse);
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
