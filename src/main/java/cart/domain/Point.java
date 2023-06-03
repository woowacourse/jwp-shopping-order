package cart.domain;

import cart.exception.OrderServerException;

import java.time.LocalDate;
import java.util.Objects;

public class Point {

    private final Long id;
    private final int value;
    private final String comment;
    private final LocalDate createAt;
    private final LocalDate expiredAt;

    private Point(Long id, int value, String comment, LocalDate createAt, LocalDate expiredAt) {
        this.id = id;
        this.value = value;
        this.comment = comment;
        this.createAt = createAt;
        this.expiredAt = expiredAt;
    }

    public static Point of(Long id, int value, String comment, LocalDate createAt, LocalDate expiredAt) {
        return new Point(id, value, comment, createAt, expiredAt);
    }

    public static Point of(int value, String comment, LocalDate expiredAt) {
        return new Point(null, value, comment, LocalDate.now(), expiredAt);
    }

    public static Point from(int value) {
        return new Point(null, value, null, null, null);
    }

    public Point subtract(Point other) {
        if (value < other.value) {
            throw new OrderServerException("포인트는 빼려고하는 값이 더 작아야합니다.");
        }
        return Point.of(id, value - other.value, comment, createAt, expiredAt);
    }

    public boolean isEmpty() {
        return value == 0;
    }

    public boolean isGreaterThan(Point other) {
        return value > other.value;
    }

    public Long getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public LocalDate getExpiredAt() {
        return expiredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return value == point.value && Objects.equals(id, point.id) && Objects.equals(comment, point.comment) && Objects.equals(createAt, point.createAt) && Objects.equals(expiredAt, point.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, comment, createAt, expiredAt);
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", value=" + value +
                ", comment='" + comment + '\'' +
                ", createAt=" + createAt +
                ", expiredAt=" + expiredAt +
                '}';
    }
}
