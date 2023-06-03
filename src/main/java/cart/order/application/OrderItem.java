package cart.order.application;

import cart.product.application.Product;

public class OrderItem {
    private Long productId;
    private Product originalProduct;
    private int quantity;

    public OrderItem(Long productId, Product originalProduct, int quantity) {
        this.productId = productId;
        this.originalProduct = originalProduct;
        this.quantity = quantity;
    }

    public static OrderItem of(Product product, int quantity) {
        return new OrderItem(product.getId(), product, quantity);
    }

    public void execute() {
        originalProduct.sold(quantity);
    }

    public int calculatePrice() {
        return originalProduct.getPrice() * quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Product getOriginalProduct() {
        return originalProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId=" + productId +
                ", originalProduct=" + originalProduct +
                ", quantity=" + quantity +
                '}';
    }
}
