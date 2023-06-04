package cart.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class OrderRequest {
    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private List<Long> cartItemIds;
    @NotBlank(message = "카드 번호는 필수 입력 값입니다.")
    private String cardNumber;
    @NotNull(message = "cvc 번호는 필수 입력 값입니다.")
    private int cvc;
    @NotNull(message = "포인트는 필수 입력 값입니다.")
    @PositiveOrZero(message = "포인트는 0 이상이어야 합니다.")
    private int point;

    public OrderRequest() {
    }

    public OrderRequest(List<Long> cartItemIds, String cardNumber, int cvc, int point) {
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
