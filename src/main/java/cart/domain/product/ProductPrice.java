package cart.domain.product;

import cart.exception.GlobalException;

public class ProductPrice {
    private static final int MIN_PRICE = 1;
    private static final int MAX_PRICE = 10_000_000;

    private final int price;

    public ProductPrice(int price) {
        validate(price);
        this.price = price;
    }

    private void validate(int price) {
        if (price < MIN_PRICE || price > MAX_PRICE) {
            throw new GlobalException("상품 가격은 1원 이상, 10,000,000원 이하여야 합니다.");
        }
    }

    public int getPrice() {
        return price;
    }
}
