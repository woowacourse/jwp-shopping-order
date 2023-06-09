package cart.dto;

public class CouponResponse {

  private final long couponId;
  private final String couponName;
  private final int minAmount;
  private final int discountAmount;
  private final boolean isPublished;

  public CouponResponse(long couponId, String couponName, int minAmount, int discountAmount, boolean isPublished) {
    this.couponId = couponId;
    this.couponName = couponName;
    this.minAmount = minAmount;
    this.discountAmount = discountAmount;
    this.isPublished = isPublished;
  }

  public long getCouponId() {
    return couponId;
  }

  public String getCouponName() {
    return couponName;
  }

  public int getMinAmount() {
    return minAmount;
  }

  public int getDiscountAmount() {
    return discountAmount;
  }

  public boolean isPublished() {
    return isPublished;
  }
}
