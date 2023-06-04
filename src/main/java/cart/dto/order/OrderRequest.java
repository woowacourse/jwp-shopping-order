package cart.dto.order;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderRequest {

    @NotNull(message = "주문 상품은 1개이상이어야 합니다.")
    final List<Long> cartItemIds;

    @Min(value = 0, message = "상품할인 금액은 음수가 될 수 없습니다.")
    final int totalItemDiscountAmount;

    @Min(value = 0, message = "멤버할인 금액은 음수가 될 수 없습니다.")
    final int totalMemberDiscountAmount;

    @Min(value = 0, message = "상품의 총 금액은 음수가 될 수 없습니다.")
    @Max(value = 1_000_000_000, message = "상품의 총 금액은 10억을 넘을 수 없습니다.")
    final int totalItemPrice;

    @Min(value = 0, message = "할인된 상품의 총 금액은 음수가 될 수 없습니다.")
    final int discountedTotalItemPrice;

    @Min(value = 0, message = "배송비는 음수가 될 수 없습니다.")
    final int shippingFee;

    @Min(value = 0, message = "총 비용은 음수가 될 수 없습니다.")
    final int totalPrice;

    public OrderRequest(
            final List<Long> cartItemIds,
            final int totalItemDiscountAmount,
            final int totalMemberDiscountAmount,
            final int totalItemPrice,
            final int discountedTotalItemPrice,
            final int shippingFee,
            final int totalPrice
    ) {
        this.cartItemIds = cartItemIds;
        this.totalItemDiscountAmount = totalItemDiscountAmount;
        this.totalMemberDiscountAmount = totalMemberDiscountAmount;
        this.totalItemPrice = totalItemPrice;
        this.discountedTotalItemPrice = discountedTotalItemPrice;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getTotalItemDiscountAmount() {
        return totalItemDiscountAmount;
    }

    public int getTotalMemberDiscountAmount() {
        return totalMemberDiscountAmount;
    }

    public int getTotalItemPrice() {
        return totalItemPrice;
    }

    public int getDiscountedTotalItemPrice() {
        return discountedTotalItemPrice;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
