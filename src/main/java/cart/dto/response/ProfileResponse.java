package cart.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ProfileResponse {

    private final int currentPoints;

    @JsonCreator
    public ProfileResponse(final int currentPoints) {
        this.currentPoints = currentPoints;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }
}
