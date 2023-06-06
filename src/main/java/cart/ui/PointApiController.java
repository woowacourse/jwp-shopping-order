package cart.ui;

import cart.application.PointService;
import cart.domain.Member;
import cart.dto.response.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
public class PointApiController {

    private final PointService pointService;

    public PointApiController(final PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public ResponseEntity<PointResponse> findOrderPolicy(Member member) {
        PointResponse response = new PointResponse(pointService.findByMember(member));
        return ResponseEntity.ok().body(response);
    }
}
