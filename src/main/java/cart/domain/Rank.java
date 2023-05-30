package cart.domain;

public enum Rank {

    NORMAL(0),
    SILVER(5),
    GOLD(10),
    PLATINUM(15),
    DIAMOND(20);

    private final int discountRate;

    Rank(int discountRate) {
        this.discountRate = discountRate;
    }

    public int getDiscountPrice(final int price) {
        return (int) (price * (1 - (double)discountRate / 100));
    }
}
