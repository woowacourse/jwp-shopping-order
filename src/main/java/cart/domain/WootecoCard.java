package cart.domain;

public class WootecoCard implements Card {
    private final String cardNumber;
    private final int cvc;

    public WootecoCard(String cardNumber, int cvc) {
        this.cardNumber = cardNumber;
        this.cvc = cvc;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
