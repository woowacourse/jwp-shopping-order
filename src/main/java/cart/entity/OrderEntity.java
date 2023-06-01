package cart.entity;

import java.util.Objects;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final int points;
    private final int savingRate;

    public OrderEntity(final Long id, final Long memberId, final int points, final int savingRate) {
        this.id = id;
        this.memberId = memberId;
        this.points = points;
        this.savingRate = savingRate;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getPoints() {
        return points;
    }

    public int getSavingRate() {
        return savingRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity other = (OrderEntity) o;
        if (id == null || other.id == null) {
            return false;
        }
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
