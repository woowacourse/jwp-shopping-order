package cart.dto.request;

import java.util.List;

public class OrderItemRequest {

    private Long id;
    private OrderProductRequest product;
    private Integer quantity;
    private List<OrderCouponRequest> coupons;

    public OrderItemRequest() {
    }


    public OrderItemRequest(Long id, OrderProductRequest product, Integer quantity, List<OrderCouponRequest> coupons) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;
    }

    public Long getId() {
        return id;
    }

    public OrderProductRequest getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public List<OrderCouponRequest> getCoupons() {
        return coupons;
    }
}
