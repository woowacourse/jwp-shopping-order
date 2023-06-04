package cart.domain.purchaseorder;

import cart.domain.Product;

public class PurchaseOrderItem {
    private Long id;
    private final Product product;
    private final Integer quantity;

    public PurchaseOrderItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public PurchaseOrderItem(Long id, Product product, Integer quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
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

    public Integer getPayment() {
        return quantity * product.getPrice();
    }

    public Long getProductId() {
        return product.getId();
    }

    @Override
    public String toString() {
        return "PurchaseOrderItem{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
