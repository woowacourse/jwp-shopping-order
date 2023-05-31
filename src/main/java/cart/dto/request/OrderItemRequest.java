package cart.dto.request;

import java.util.List;

public class OrderItemRequest {

    private OrderProductRequest product;
    private Integer quantity;
    private List<OrderCouponRequest> coupons;
    private Integer totalPrice;

    public OrderItemRequest() {
    }

    public OrderItemRequest(OrderProductRequest product, Integer quantity, List<OrderCouponRequest> coupons,
                            Integer totalPrice) {
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;
        this.totalPrice = totalPrice;
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

    public Integer getTotalPrice() {
        return totalPrice;
    }
}
