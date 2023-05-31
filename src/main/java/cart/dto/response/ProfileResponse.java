package cart.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ProfileResponse {

    private final int points;

    @JsonCreator
    public ProfileResponse(final int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
