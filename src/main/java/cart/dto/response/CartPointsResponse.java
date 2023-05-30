package cart.dto.response;

public class CartPointsResponse {
    private final int savingRate;
    private final int points;

    public CartPointsResponse(final int savingRate, final int points) {
        this.savingRate = savingRate;
        this.points = points;
    }

    public int getSavingRate() {
        return savingRate;
    }

    public int getPoints() {
        return points;
    }
}
