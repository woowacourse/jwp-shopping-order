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

    public long calculateTotalPrice() {
        return quantity * product.getPrice().getValue();
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderedItem{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
