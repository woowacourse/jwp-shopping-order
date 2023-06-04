package cart.domain;

public class ProductOrder {

  private final Long id;
  private final Product product;
  private final Order order;
  private final int quantity;

  public ProductOrder(Long id, Product product, Order order, int quantity) {
    this.id = id;
    this.product = product;
    this.order = order;
    this.quantity = quantity;
  }

  public Long getId() {
    return id;
  }

  public Product getProduct() {
    return product;
  }

  public Order getOrder() {
    return order;
  }

  public int getQuantity() {
    return quantity;
  }
}
