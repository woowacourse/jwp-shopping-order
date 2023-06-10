package cart.domain;

import java.util.ArrayList;
import java.util.List;

public class OrderedItems {
    private final List<OrderedItem> orderedItems;

    public OrderedItems(List<OrderedItem> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public OrderedItems merge(OrderedItems other) {
        List<OrderedItem> mergedList = new ArrayList<>(this.orderedItems);
        mergedList.addAll(other.getOrderedItems());
        return new OrderedItems(mergedList);
    }

    public Integer calculateTotalPrice() {
        return orderedItems.stream()
                .mapToInt(OrderedItem::calculateTotalPrice)
                .sum();
    }

    public List<OrderedItem> getOrderedItems() {
        return orderedItems;
    }
}
