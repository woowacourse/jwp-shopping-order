package cart.entity;

import java.time.LocalDate;
import java.util.Objects;

public class PointEntity {

    private final Long id;
    private final int value;
    private final String comment;
    private final LocalDate createAt;
    private final LocalDate expiredAt;

    public PointEntity(Long id, int value, String comment, LocalDate createAt, LocalDate expiredAt) {
        this.id = id;
        this.value = value;
        this.comment = comment;
        this.createAt = createAt;
        this.expiredAt = expiredAt;
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
        PointEntity that = (PointEntity) o;
        return value == that.value && Objects.equals(id, that.id) && Objects.equals(comment, that.comment) && Objects.equals(createAt, that.createAt) && Objects.equals(expiredAt, that.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, comment, createAt, expiredAt);
    }


}
