package cart.domain;

import java.util.List;
import java.util.stream.Collectors;

public class CartItems {
    private final List<CartItem> items;

    public CartItems(final List<CartItem> items) {
        this.items = items;
    }

    public Price calculateTotalPrice() {
        return items.stream()
                .map(CartItem::calculatePrice)
                .reduce(Price.ZERO_PRICE, Price::sum);
    }

    public CartItems getSubCartItems(List<Long> cartItemIds) {
        return new CartItems(items.stream()
                .filter(item -> cartItemIds.contains(item.getId()))
                .collect(Collectors.toList()));
    }

    public List<CartItem> getItems() {
        return items;
    }
}
