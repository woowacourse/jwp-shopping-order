package cart.dto;

import java.util.List;

public class OrderRequest {

    private ProductRequest.WithId product;
    private Integer quantity;
    private List<Long> couponId;

    private OrderRequest() {
    }

    public OrderRequest(ProductRequest.WithId product, Integer quantity, List<Long> couponId) {
        this.product = product;
        this.quantity = quantity;
        this.couponId = couponId;
    }

    public ProductRequest.WithId getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public List<Long> getCouponId() {
        return couponId;
    }
}
