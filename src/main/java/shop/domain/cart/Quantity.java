package shop.domain.cart;

import shop.exception.ShoppingException;

public class Quantity {
    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 1_000;

    private final int quantity;

    public Quantity(int quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(int quantity) {
        if (quantity > MAX_QUANTITY) {
            throw new ShoppingException("해당 상품을 더 담을 수 없습니다. 하나의 상품은 최대 1,000개 까지만 담을 수 있습니다.");
        }
        if (quantity < MIN_QUANTITY) {
            throw new ShoppingException("장바구니에 상품을 담기 위해서는 최소 한 개 이상을 선택해야 합니다.");
        }
    }

    public int getQuantity() {
        return quantity;
    }
}
