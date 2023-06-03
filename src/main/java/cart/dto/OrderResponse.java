package cart.dto;

import java.util.List;

public class OrderResponse {

    private Long id;
    private Integer totalProductAmount;
    private Integer discountedProductAmount;
    private Integer deliveryAmount;
    private String address;
    private List<OrderProductResponse> products;

    public OrderResponse() {
    }

    public OrderResponse(final Long id, final Integer totalProductAmount, final Integer discountedProductAmount,
        final Integer deliveryAmount,
        final String address, final List<OrderProductResponse> products) {
        this.id = id;
        this.totalProductAmount = totalProductAmount;
        this.discountedProductAmount = discountedProductAmount;
        this.deliveryAmount = deliveryAmount;
        this.address = address;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public Integer getTotalProductAmount() {
        return totalProductAmount;
    }

    public Integer getDeliveryAmount() {
        return deliveryAmount;
    }

    public Integer getDiscountedProductAmount() {
        return discountedProductAmount;
    }

    public String getAddress() {
        return address;
    }

    public List<OrderProductResponse> getProducts() {
        return products;
    }
}
