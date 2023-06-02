package cart.coupon.dao;

import java.math.BigDecimal;

public class CouponEntity {

  private Long id;
  private String name;
  private BigDecimal discountPrice;

  public CouponEntity(final Long id, final String name, final BigDecimal discountPrice) {
    this.id = id;
    this.name = name;
    this.discountPrice = discountPrice;
  }

  public CouponEntity(final String name, final BigDecimal discountPrice) {
    this.name = name;
    this.discountPrice = discountPrice;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getDiscountPrice() {
    return discountPrice;
  }
}
