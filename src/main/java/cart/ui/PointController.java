package cart.ui;

import cart.domain.Member;
import cart.domain.Point;
import cart.domain.PointPolicy;
import cart.dto.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/points")
public class PointController {

    @GetMapping
    public ResponseEntity<PointResponse> getPoint(Member member) {
        Point memberPoint = member.getPoint();
        Point minUsagePoint = PointPolicy.MINIMUM_POINT_USAGE_STANDARD;
        PointResponse response = new PointResponse(memberPoint.getValue(), minUsagePoint.getValue());

        return ResponseEntity.ok(response);
    }
}
