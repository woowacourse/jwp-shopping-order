package cart.dto;

public class SaveCouponRequest {

  private final String name;
  private final int minAmount;
  private final int discountAmount;

  public SaveCouponRequest(String name, int minAmount, int discountAmount) {
    this.name = name;
    this.minAmount = minAmount;
    this.discountAmount = discountAmount;
  }

  public String getName() {
    return name;
  }

  public int getMinAmount() {
    return minAmount;
  }

  public int getDiscountAmount() {
    return discountAmount;
  }
}
