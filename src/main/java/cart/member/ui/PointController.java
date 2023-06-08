package cart.member.ui;

import cart.member.ui.dto.PointDto;
import cart.member.domain.Member;
import cart.member.domain.Point;
import cart.member.domain.PointPolicy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/points")
public class PointController {

    @GetMapping
    public ResponseEntity<PointDto> getPoint(Member member) {
        Point memberPoint = member.getPoint();
        Point minUsagePoint = PointPolicy.MINIMUM_POINT_USAGE_STANDARD;
        PointDto response = new PointDto(memberPoint.getValue(), minUsagePoint.getValue());

        return ResponseEntity.ok(response);
    }
}
