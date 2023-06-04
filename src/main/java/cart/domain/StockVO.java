package cart.domain;

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
}
