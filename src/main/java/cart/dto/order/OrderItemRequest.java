package cart.dto.order;

import cart.dto.coupon.MemberCouponRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemRequest {

    @NotNull(message = "ID는 필수 입력입니다. 반드시 입력해주세요.")
    @Positive(message = "유효한 ID를 입력해주세요.")
    private Long id;
    private OrderProductRequest product;
    @Min(value = 0, message = "상품 개수는 음수일 수 없습니다.")
    private int quantity;
    @Valid
    @NotNull(message = "쿠폰 필드가 누락되었습니다.")
    private List<MemberCouponRequest> coupons;

    private OrderItemRequest() {
    }

    public OrderItemRequest(final Long id, final OrderProductRequest product, final int quantity, final List<MemberCouponRequest> coupons) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;
    }

    public List<Long> toMemberCouponIds() {
        return coupons.stream()
                .map(MemberCouponRequest::getCouponId)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public OrderProductRequest getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<MemberCouponRequest> getCoupons() {
        return coupons;
    }
}
