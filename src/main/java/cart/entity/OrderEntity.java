package cart.entity;

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
}
