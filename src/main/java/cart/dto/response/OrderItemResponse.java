package cart.dto.response;

import cart.dto.request.OrderCouponRequest;
import java.util.List;

public class OrderItemResponse {

    private Long id;
    private OrderProductResponse product;
    private Integer quantity;
    private List<OrderCouponRequest> coupons;
    private Integer totalPrice;

    public OrderItemResponse() {
    }

    public OrderItemResponse(Long id, OrderProductResponse product, Integer quantity, List<OrderCouponRequest> coupons,
                             Integer totalPrice) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public OrderProductResponse getProduct() {
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
