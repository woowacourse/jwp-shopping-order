package cart.dto.response;

public class CartPointsResponse {

    private final int savingRate;
    private final int expectedSavePoints;

    public CartPointsResponse(final int savingRate, final int expectedSavePoints) {
        this.savingRate = savingRate;
        this.expectedSavePoints = expectedSavePoints;
    }

    public int getSavingRate() {
        return savingRate;
    }

    public int getExpectedSavePoints() {
        return expectedSavePoints;
    }
}
