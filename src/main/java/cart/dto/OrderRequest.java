package cart.dto;

import java.util.List;

public class OrderRequest {
    private List<Long> cartItemIds;
    private String cardNumber;
    private int cvc;
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
