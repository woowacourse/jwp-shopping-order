package cart.dto;

import java.util.List;

public class OrderResponse {

    private Long id;
    private Integer totalProductAmount;
    private Integer deliveryAmount;
    private Integer discountedProductAmount;
    private String address;
    private List<OrderProductResponse> products;

    public OrderResponse() {
    }

    public OrderResponse(final Long id, final Integer totalProductAmount, final Integer deliveryAmount,
        final Integer discountedProductAmount,
        final String address, final List<OrderProductResponse> products) {
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
