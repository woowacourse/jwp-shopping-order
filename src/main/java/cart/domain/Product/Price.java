package cart.domain.Product;

import cart.domain.Point;

public class Price {
    private final int price;

    public Price(int price) {
        this.price = price;
    }

    public static Price from(int price) {
        validatePrice(price);
        return new Price(price);
    }

    private static void validatePrice(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("가격은 0보다 작을 수 없습니다.");
        }
    }

    public Point makePointFrom(double rate) {
        int point = (int) Math.round(this.price * rate);
        return new Point(point);
    }

    public Price multiply(int quantity) {
        return new Price(price * quantity);
    }

    public Price subtract(Point point){
        return new Price(this.price - point.point());
    }

    public Price sum(Price other) {
        return new Price(this.price + other.price);
    }

    public int price() {
        return price;
    }
}
