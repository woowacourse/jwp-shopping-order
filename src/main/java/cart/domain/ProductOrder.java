package cart.domain;

public class ProductOrder {

  private final Long id;
  private final Product product;
  private final Long orderId;
  private final int quantity;

  public ProductOrder(Long id, Product product, Long orderId, int quantity) {
    this.id = id;
    this.product = product;
    this.orderId = orderId;
    this.quantity = quantity;
  }

  public Long getId() {
    return id;
  }

  public Product getProduct() {
    return product;
  }

  public Long getOrderId() {
    return orderId;
  }

  public int getQuantity() {
    return quantity;
  }
}
