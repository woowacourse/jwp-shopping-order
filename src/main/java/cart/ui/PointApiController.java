package cart.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cart.application.PointService;
import cart.application.dto.GetPointResponse;
import cart.domain.Member;

@RestController
public class PointApiController {

    private final PointService pointService;

    public PointApiController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/point")
    public ResponseEntity<GetPointResponse> showPoint(Member member) {
        return ResponseEntity.ok(pointService.getPointStatus(member));
    }
}
