package cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PointResponse {

    private final int userPoint;
    private final int minUsagePoint;
}
