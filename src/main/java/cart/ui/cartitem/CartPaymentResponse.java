package cart.ui.cartitem;

public class CartPaymentResponse {

    private int paymentPrice;

    public CartPaymentResponse() {
    }

    public CartPaymentResponse(final int paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public int getPaymentPrice() {
        return paymentPrice;
    }

}