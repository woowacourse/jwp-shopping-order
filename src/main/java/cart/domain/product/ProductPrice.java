package cart.domain.product;

public class ProductPrice {

    private final int price;

    public ProductPrice(final int price) {
        validatePositive(price);
        this.price = price;
    }

    private void validatePositive(int target) {
        if (target <= 0) {
            throw new IllegalArgumentException("가격은 양수여야합니다.");
        }
    }

    public int getPrice() {
        return price;
    }
}
