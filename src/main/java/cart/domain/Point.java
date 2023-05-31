package cart.domain;

public class Point implements Money {
    private Long value;

    public Point() {
    }

    public Point(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Point{" +
                "value=" + value +
                '}';
    }
}
