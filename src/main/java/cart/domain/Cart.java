package cart.domain;

public class Cart {

    private final Items items;

    public Cart(final Items items) {
        this.items = items;
    }

    public Items getItems() {
        return items;
    }
}
