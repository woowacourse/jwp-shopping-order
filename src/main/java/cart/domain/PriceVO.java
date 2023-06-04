package cart.domain;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceVO)) return false;
        PriceVO priceVO = (PriceVO) o;
        return price == priceVO.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
