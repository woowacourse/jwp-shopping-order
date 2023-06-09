package cart.ui;

import cart.application.PointService;
import cart.domain.Member;
import cart.dto.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/points")
public class PointApiController {

    private final PointService pointService;

    public PointApiController(final PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public ResponseEntity<PointResponse> findPoint(Member member){
        final PointResponse point = pointService.findPointByMember(member);
        return ResponseEntity.ok(point);
    }


}
