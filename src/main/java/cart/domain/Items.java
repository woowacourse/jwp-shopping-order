package cart.domain;

import java.util.List;

public class Items {

    private final List<Item> items;

    public Items(final List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }
}
