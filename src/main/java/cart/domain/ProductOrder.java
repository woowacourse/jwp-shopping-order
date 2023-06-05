package cart.domain;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductOrder that = (ProductOrder) o;
    return getQuantity() == that.getQuantity() && Objects.equals(getProduct(), that.getProduct())
        && Objects.equals(getOrder(), that.getOrder());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getProduct(), getOrder(), getQuantity());
  }
}
