package cart.dto;

import java.util.List;

public class OrderResponse {

    private final Long id;
    private final Integer totalProductAmount;
    private final Integer deliveryAmount;
    private final Integer discountedProductAmount;
    private final String address;
    private final List<OrderProductResponse> products;

    public OrderResponse(final Long id, final Integer totalProductAmount, final Integer deliveryAmount,
        final Integer discountedProductAmount, final String address,
        final List<OrderProductResponse> products) {
        this.id = id;
        this.totalProductAmount = totalProductAmount;
        this.deliveryAmount = deliveryAmount;
        this.discountedProductAmount = discountedProductAmount;
        this.address = address;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public Integer getTotalAmount() {
        return totalProductAmount;
    }

    public Integer getDeliveryAmount() {
        return deliveryAmount;
    }

    public Integer getDiscountedAmount() {
        return discountedProductAmount;
    }

    public String getAddress() {
        return address;
    }

    public List<OrderProductResponse> getProducts() {
        return products;
    }
}
