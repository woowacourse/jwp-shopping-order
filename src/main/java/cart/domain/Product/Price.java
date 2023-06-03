package cart.domain.Product;

import cart.domain.Point;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return price == price1.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
