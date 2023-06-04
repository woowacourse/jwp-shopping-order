package cart.dto;

import java.util.List;

public class AllOrderResponse {

  private final long id;
  private final List<OrderProductResponse> products;

  public AllOrderResponse(long id, List<OrderProductResponse> products) {
    this.id = id;
    this.products = products;
  }

  public long getId() {
    return id;
  }

  public List<OrderProductResponse> getProducts() {
    return products;
  }
}
