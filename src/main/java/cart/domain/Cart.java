package cart.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Cart {

    private final List<CartItem> items;

    public Cart(List<CartItem> items) {
        this.items = items;
    }

    public void validateOwner(Member member) {
        items.forEach(item -> item.checkOwner(member));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public List<Long> getCartItemIds() {
        return items.stream()
            .map(CartItem::getId)
            .collect(Collectors.toList());
    }
}
