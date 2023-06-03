package cart.domain;

public class Order {

  private final Long id;
  private final Products products;
  private final Coupon coupon;
  private final Amount deliveryAmount;
  private final String address;

  public Order(Products products, Coupon coupon, Amount deliveryAmount, String address) {
    this(null, products, coupon, deliveryAmount, address);
  }

  public Order(Long id, Products products, Coupon coupon, Amount deliveryAmount, String address) {
    this.id = id;
    this.products = products;
    this.coupon = coupon;
    this.deliveryAmount = deliveryAmount;
    this.address = address;
  }

  public Amount calculateDiscountedAmount() {
    final Amount sumPrice = products.sumPrice();
    return coupon.apply(sumPrice);
  }

  public Long getId() {
    return id;
  }

  public Products getProducts() {
    return products;
  }

  public Coupon getCoupon() {
    return coupon;
  }

  public Amount getDeliveryAmount() {
    return deliveryAmount;
  }

  public String getAddress() {
    return address;
  }
}
