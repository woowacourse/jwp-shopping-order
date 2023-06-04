package cart.domain.order;

import java.util.Objects;

public class SavedPoint {

    private final int savedPoint;

    public SavedPoint(final int savedPoint) {
        this.savedPoint = savedPoint;
    }

    public int getSavedPoint() {
        return savedPoint;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SavedPoint other = (SavedPoint) o;
        return savedPoint == other.savedPoint;
    }

    @Override
    public int hashCode() {
        return Objects.hash(savedPoint);
    }

    @Override
    public String toString() {
        return "SavedPoint{" +
                "savedPoint=" + savedPoint +
                '}';
    }
}
