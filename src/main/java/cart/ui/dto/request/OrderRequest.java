package cart.ui.dto.request;

import java.util.List;

public class OrderRequest {

    private List<CartItemRequest> products;
    private int totalProductAmount;
    private int deliveryAmount;
    private String address;
    private Long couponId;

    public OrderRequest() {
    }

    public OrderRequest(final List<CartItemRequest> products, final int totalProductAmount, final int deliveryAmount,
        final String address, final Long couponId) {
        this.products = products;
        this.totalProductAmount = totalProductAmount;
        this.deliveryAmount = deliveryAmount;
        this.address = address;
        this.couponId = couponId;
    }

    public List<CartItemRequest> getProducts() {
        return products;
    }

    public int getTotalProductAmount() {
        return totalProductAmount;
    }

    public int getDeliveryAmount() {
        return deliveryAmount;
    }

    public String getAddress() {
        return address;
    }

    public Long getCouponId() {
        return couponId;
    }
}
