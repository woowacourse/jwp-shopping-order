package cart.domain;

import java.util.List;
import java.util.Objects;

public class Order {

  private final Long id;
  private final Member member;
  private final Products products;
  private final Coupon coupon;
  private final Amount deliveryAmount;
  private final String address;

  public Order(Products products, Coupon coupon, Amount deliveryAmount, String address, Member member) {
    this(null, member, products, coupon, deliveryAmount, address);
  }

  public Order(Long id, Member member, Products products, Coupon coupon, Amount deliveryAmount, String address) {
    this.id = id;
    this.member = member;
    this.products = products;
    this.coupon = coupon;
    this.deliveryAmount = deliveryAmount;
    this.address = address;
  }

  public Amount calculateTotalProductAmount(final List<Integer> quantities) {
    Amount sum = new Amount(0);
    final List<Product> values = products.getValues();
    for (int index = 0; index < values.size(); index++) {
      sum = sum.plus(values.get(index).getPrice().multiply(quantities.get(index)));
    }
    return sum;
  }

  public Amount calculateDiscountedAmount(final List<Integer> quantities) {
    final Amount totalProductAmount = calculateTotalProductAmount(quantities);
    if (coupon.isEmpty()) {
      return totalProductAmount;
    }
    return coupon.apply(totalProductAmount);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(getId(), order.getId()) && Objects.equals(getMember(), order.getMember())
        && Objects.equals(getProducts(), order.getProducts()) && Objects.equals(getCoupon(),
        order.getCoupon()) && Objects.equals(getDeliveryAmount(), order.getDeliveryAmount())
        && Objects.equals(getAddress(), order.getAddress());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getMember(), getProducts(), getCoupon(), getDeliveryAmount(), getAddress());
  }

  public Long getId() {
    return id;
  }

  public Member getMember() {
    return member;
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
