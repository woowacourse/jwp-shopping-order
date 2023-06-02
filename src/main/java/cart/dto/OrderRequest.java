package cart.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderRequest {
    @NotNull
    private List<Long> cartItemIds;
    @NotBlank(message = "카드 번호를 입력해주세요")
    private String cardNumber;
    @NotNull
    private int cvc;
    @NotNull
    private long point;

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getCvc() {
        return cvc;
    }

    public long getPoint() {
        return point;
    }
}
