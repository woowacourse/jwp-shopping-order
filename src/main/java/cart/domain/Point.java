package cart.domain;

import java.time.LocalDate;

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

    public static Point of(int value, String comment, PointExpirePolicy pointExpirePolicy) {
        LocalDate createAt = LocalDate.now();
        return new Point(null, value, comment, createAt, calculateExpiredAt(createAt, pointExpirePolicy));
    }

    private static LocalDate calculateExpiredAt(LocalDate createAt, PointExpirePolicy pointExpirePolicy) {
        return pointExpirePolicy.calculateExpireDate(createAt);
    }
}
