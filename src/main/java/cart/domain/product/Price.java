package cart.domain.product;

import cart.exception.badrequest.product.ProductPriceException;

class Price {

    private static final int MINIMUM_VALUE = 0;

    private final int value;

    public Price(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM_VALUE) {
            throw new ProductPriceException("상품 가격은 음수일 수 없습니다. 현재 상품 가격: " + value);
        }
    }

    public int getValue() {
        return value;
    }
}
