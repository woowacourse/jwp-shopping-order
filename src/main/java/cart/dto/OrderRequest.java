package cart.dto;

import java.util.List;

public class OrderRequest {

    private Long cartItemId;
    private ProductRequest product;
    private Integer quantity;
    private List<Long> couponIds;

    private OrderRequest() {
    }

    public OrderRequest(Long cartItemId, ProductRequest product, Integer quantity, List<Long> couponIds) {
        this.cartItemId = cartItemId;
        this.product = product;
        this.quantity = quantity;
        this.couponIds = couponIds;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public ProductRequest getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }
}
