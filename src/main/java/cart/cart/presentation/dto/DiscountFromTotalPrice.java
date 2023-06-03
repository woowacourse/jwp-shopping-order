package cart.cart.presentation.dto;

public class DiscountFromTotalPrice {
    private int discountPrice;

    public DiscountFromTotalPrice() {
    }

    public DiscountFromTotalPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
