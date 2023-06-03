package cart.dto;

import java.util.Objects;

public class PointResponse {

    private int currentPoint;
    private int toBeExpiredPoint;

    public PointResponse(int currentPoint, int toBeExpiredPoint) {
        this.currentPoint = currentPoint;
        this.toBeExpiredPoint = toBeExpiredPoint;
    }

    public int getCurrentPoint() {
        return currentPoint;
    }

    public int getToBeExpiredPoint() {
        return toBeExpiredPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointResponse that = (PointResponse) o;
        return currentPoint == that.currentPoint && toBeExpiredPoint == that.toBeExpiredPoint;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPoint, toBeExpiredPoint);
    }

    @Override
    public String toString() {
        return "PointResponse{" +
                "currentPoint=" + currentPoint +
                ", toBeExpiredPoint=" + toBeExpiredPoint +
                '}';
    }
}
