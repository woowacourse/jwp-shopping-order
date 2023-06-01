package cart.ui.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장바구니 상품 목록 금액 응답")
public class CartItemPriceResponse {

    @Schema(description = "장바구니 상품 목록 총 금액", example = "48000")
    private int totalPrice;

    @Schema(description = "장바구니 상품 목록 배송료", example = "3000")
    private int deliveryFee;

    private CartItemPriceResponse() {
    }

    public CartItemPriceResponse(int totalPrice, int deliveryFee) {
        this.totalPrice = totalPrice;
        this.deliveryFee = deliveryFee;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
