package cart.domain.Product;

public class Price {
    private final int price;

    public Price(int price) {
        this.price = price;
    }

    public static Price from(int price){
        validatePrice(price);
        return new Price(price);
    }

    private static void validatePrice(int price) {
        if(price < 0) {
            throw new IllegalArgumentException("가격은 0보다 작을 수 없습니다.");
        }
    }

    public int price() {
        return price;
    }
}
