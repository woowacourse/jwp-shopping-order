package cart.domain;

import java.util.Objects;

public final class StockVO {

    private int stock;

    public StockVO(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("재고는 음수가 될 수 없습니다.");
        }
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockVO)) return false;
        StockVO stockVO = (StockVO) o;
        return stock == stockVO.stock;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stock);
    }
}
