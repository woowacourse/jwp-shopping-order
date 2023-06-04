package cart.ui;

import cart.application.PointService;
import cart.domain.Member;
import cart.dto.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("points")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public ResponseEntity<PointResponse> getPoints(Member member) {
        PointResponse pointResponse = pointService.findBy(member);
        return ResponseEntity.ok().body(pointResponse);
    }

}
