package cart.dto.order;

import cart.dto.coupon.MemberCouponRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemRequest {

    @JsonProperty("id")
    @NotNull(message = "ID는 필수 입력입니다. 반드시 입력해주세요.")
    @Positive(message = "유효한 ID를 입력해주세요.")
    private Long cartItemId;
    @JsonProperty("product")
    private OrderProductRequest productRequest;
    @Min(value = 0, message = "상품 개수는 음수일 수 없습니다.")
    private int quantity;
    @Valid
    @NotNull(message = "쿠폰 필드가 누락되었습니다.")
    @JsonProperty("coupons")
    private List<MemberCouponRequest> memberCoupons;

    private OrderItemRequest() {
    }

    public OrderItemRequest(final Long cartItemId, final OrderProductRequest productRequest, final int quantity, final List<MemberCouponRequest> memberCoupons) {
        this.cartItemId = cartItemId;
        this.productRequest = productRequest;
        this.quantity = quantity;
        this.memberCoupons = memberCoupons;
    }

    public List<Long> toMemberCouponIds() {
        return memberCoupons.stream()
                .map(MemberCouponRequest::getId)
                .collect(Collectors.toList());
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public OrderProductRequest getProductRequest() {
        return productRequest;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<MemberCouponRequest> getMemberCoupons() {
        return memberCoupons;
    }
}
