package cart.domain;

import cart.exception.BusinessException;
import java.util.List;
import java.util.Objects;

public class Order {

  private final Long id;
  private final Member member;
  private final List<ProductOrder> products;
  private final Coupon coupon;
  private final Amount deliveryAmount;
  private final String address;

  public Order(List<ProductOrder> products, Coupon coupon, Amount deliveryAmount, String address, Member member) {
    this(null, member, products, coupon, deliveryAmount, address);
  }

  public Order(Long id, Member member, List<ProductOrder> products, Coupon coupon, Amount deliveryAmount, String address) {
    this.id = id;
    this.member = member;
    this.products = products;
    this.coupon = coupon;
    this.deliveryAmount = deliveryAmount;
    this.address = address;
  }

  public Amount calculateTotalProductAmount() {
    Amount sum = new Amount(0);
    for (ProductOrder productOrder : products) {
      sum = sum.plus(productOrder.calculateAmount());
    }
    return sum;
  }

  public Amount calculateDiscountedAmount() {
    final Amount totalProductAmount = calculateTotalProductAmount();
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

  public Coupon getCoupon() {
    return coupon;
  }

  public List<ProductOrder> getProducts() {
    return products;
  }

  public Amount getDeliveryAmount() {
    return deliveryAmount;
  }

  public String getAddress() {
    return address;
  }

  public void checkOwner(Member member) {
    if (!Objects.equals(this.member.getId(), member.getId())) {
      throw new BusinessException("주문의 주인이 아닙니다.");
    }
  }
}
