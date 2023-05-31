package cart.dto;

import java.util.List;

public class OrderCreateRequest {

    private List<Integer> cartItemIds;
    private String cardNumber;
    private int cvc;
    private int point;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(final List<Integer> cartItemIds, final String cardNumber, final int cvc,
                              final int point) {
        this.cartItemIds = cartItemIds;
        this.cardNumber = cardNumber;
        this.cvc = cvc;
        this.point = point;
    }

    public List<Integer> getCartItemIds() {
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
