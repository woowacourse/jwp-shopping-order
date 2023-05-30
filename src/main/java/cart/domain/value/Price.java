package cart.domain.value;

public class Price {

    private final int price;

    public Price(final int price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(final int price) {
        if (price < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
    }

    public int getPrice() {
        return price;
    }
}
