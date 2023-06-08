package cart.member.ui.dto;

public class PointDto {
    private final int userPoint;
    private final int minUsagePoint;

    public PointDto(int userPoint, int minUsagePoint) {
        this.userPoint = userPoint;
        this.minUsagePoint = minUsagePoint;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public int getMinUsagePoint() {
        return minUsagePoint;
    }
}
