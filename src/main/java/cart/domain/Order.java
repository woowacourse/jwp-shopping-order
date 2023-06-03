package cart.domain;

public class Order {

  private final Products products;
  private final Coupon coupon;

  public Order(Products products, final Coupon coupon) {
    this.products = products;
    this.coupon = coupon;
  }

  public Amount calculateDiscountedAmount() {
    final Amount sumPrice = products.sumPrice();
    return coupon.apply(sumPrice);
  }

  public Products getProducts() {
    return products;
  }

  public Coupon getCoupon() {
    return coupon;
  }
}
