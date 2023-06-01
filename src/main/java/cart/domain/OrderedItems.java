package cart.domain;

import java.util.List;

public class OrderedItems {
    private final List<OrderedItem> orderedItems;

    public OrderedItems(List<OrderedItem> orderedItems) {
        this.orderedItems = orderedItems;
    }
}
