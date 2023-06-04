package cart.dto;

public class DiscountAmountResponse {

  private final int discountedProductAmount;
  private final int discountAmount;

  public DiscountAmountResponse(int discountedProductAmount, int discountAmount) {
    this.discountedProductAmount = discountedProductAmount;
    this.discountAmount = discountAmount;
  }

  public int getDiscountedProductAmount() {
    return discountedProductAmount;
  }

  public int getDiscountAmount() {
    return discountAmount;
  }
}
