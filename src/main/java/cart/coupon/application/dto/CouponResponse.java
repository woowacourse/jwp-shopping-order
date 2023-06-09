package cart.coupon.application.dto;

import java.math.BigDecimal;

public class CouponResponse {

  private Long id;
  private String name;
  private BigDecimal priceDiscount;

  private CouponResponse() {
  }

  public CouponResponse(final Long id, final String name, final BigDecimal priceDiscount) {
    this.id = id;
    this.name = name;
    this.priceDiscount = priceDiscount;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPriceDiscount() {
    return priceDiscount;
  }
}
