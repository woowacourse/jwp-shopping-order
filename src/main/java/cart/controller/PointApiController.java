package cart.controller;

import cart.auth.Auth;
import cart.domain.member.Member;
import cart.domain.point.Point;
import cart.dto.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/points")
public class PointApiController {

    @GetMapping
    public ResponseEntity<PointResponse> getAvailablePoint(@Auth final Member member) {
        final Point point = member.getPoint();
        final PointResponse pointResponse = new PointResponse(point.getMoneyAmount());
        return ResponseEntity.ok(pointResponse);
    }
}
