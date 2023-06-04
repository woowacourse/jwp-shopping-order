package cart.domain;

import java.util.List;

public class Items {

    private final List<Item> items;

    public Items(final List<Item> items) {
        validateItemsSize(items);
        this.items = items;
    }

    private void validateItemsSize(final List<Item> items) {
        int totalQuantity = calculateItemsQuantity(items);
        if (totalQuantity < 1 || totalQuantity > 1000) {
            throw new IllegalArgumentException("상품 총 수량은 1개 이상 1000개 이하까지 가능합니다.");
        }
    }

    private int calculateItemsQuantity(final List<Item> items) {
        return items.stream()
                .mapToInt(Item::getQuantity)
                .sum();
    }

    public int calculateItemsPrice(final List<Item> items) {
        return items.stream()
                .mapToInt(Item::calculateItemPrice)
                .sum();
    }

    public List<Item> getItems() {
        return items;
    }
}
