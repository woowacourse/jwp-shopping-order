package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "주문 추가 요청")
public class OrderCreateRequest {

    @Schema(description = "장바구니 아이템 아이디 목록", example = "1, 2, 3")
    private List<Long> cartItemIds;
    @Schema(description = "결제할 카드 번호", example = "4955-2939-3293-3923")
    private String cardNumber;
    @Schema(description = "결제할 카드의 cvc 번호", example = "345")
    private int cvc;
    @Schema(description = "결제에 사용할 포인트", example = "400")
    private int point;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(final List<Long> cartItemIds, final String cardNumber, final int cvc,
                              final int point) {
        this.cartItemIds = cartItemIds;
        this.cardNumber = cardNumber;
        this.cvc = cvc;
        this.point = point;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getCvc() {
        return cvc;
    }

    public int getPoint() {
        return point;
    }
}
