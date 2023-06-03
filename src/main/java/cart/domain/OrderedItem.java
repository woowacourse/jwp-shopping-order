package cart.domain;

public class OrderedItem {
    private final Long id;
    private final Product product;
    private final Integer quantity;

    public OrderedItem(Long id, Product product, Integer quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }
}
