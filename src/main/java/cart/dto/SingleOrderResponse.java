package cart.dto;

import java.util.List;

public class SingleOrderResponse {

  private final long id;
  private final List<OrderProductResponse> products;
  private final Integer totalProductAmount;
  private final Integer deliveryAmount;
  private final Integer discountedProductAmount;
  private final String address;

  public SingleOrderResponse(long id, List<OrderProductResponse> products, Integer totalProductAmount,
      Integer deliveryAmount, Integer discountedProductAmount, String address) {
    this.id = id;
    this.products = products;
    this.totalProductAmount = totalProductAmount;
    this.deliveryAmount = deliveryAmount;
    this.discountedProductAmount = discountedProductAmount;
    this.address = address;
  }

  public long getId() {
    return id;
  }

  public List<OrderProductResponse> getProducts() {
    return products;
  }

  public Integer getTotalProductAmount() {
    return totalProductAmount;
  }

  public Integer getDeliveryAmount() {
    return deliveryAmount;
  }

  public Integer getDiscountedProductAmount() {
    return discountedProductAmount;
  }

  public String getAddress() {
    return address;
  }
}
