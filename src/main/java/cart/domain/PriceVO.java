package cart.domain;

public final class PriceVO {

    private final int price;

    public PriceVO(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
