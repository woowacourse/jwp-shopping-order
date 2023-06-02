package cart.dto;

import java.util.List;

public class OrderItemRequest {

    private Long id;
    private ProductRequest.WithId product;
    private Integer quantity;
    private List<MemberCouponRequest> coupons;

    private OrderItemRequest() {
    }

    public OrderItemRequest(Long id, ProductRequest.WithId product, Integer quantity, List<MemberCouponRequest> coupons) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;
    }

    public Long getId() {
        return id;
    }

    public ProductRequest.WithId getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public List<MemberCouponRequest> getCoupons() {
        return coupons;
    }
}
