package cart.dto.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OrderItemRequest {

    @NotBlank(message = "주문 상품의 아이디를 입력해주세요.")
    private Long id;

    @NotNull(message = "주문 상품 정보를 입력해주세요")
    private OrderProductRequest product;

    @NotNull(message = "상품 수량을 입력해주세요.")
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
