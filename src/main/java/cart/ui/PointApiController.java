package cart.ui;

import cart.domain.Member;
import cart.domain.Point;
import cart.dto.response.PointResponse;
import cart.dto.response.SavingPointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointApiController {
    @GetMapping("/points")
    public ResponseEntity<PointResponse> pointDetail(Member member) {
        int point = member.getPoint().getPoint();
        return ResponseEntity.ok(new PointResponse(point));
    }

    @GetMapping("/saving-point")
    public ResponseEntity<SavingPointResponse> pointSaved(@RequestParam Integer totalPrice) {
        int point = Point.calcualtePoint(totalPrice).getPoint();
        return ResponseEntity.ok(new SavingPointResponse(point));
    }
}
