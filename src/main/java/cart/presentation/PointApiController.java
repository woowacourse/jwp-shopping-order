package cart.presentation;

import cart.domain.Member;
import cart.dto.response.PointResponse;
import cart.service.PointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/points")
public class PointApiController {

    private final PointService pointService;

    public PointApiController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public ResponseEntity<PointResponse> getUserPoint(final Member member) {
        int userPoint = pointService.getUserPoint(member);
        PointResponse response = new PointResponse(userPoint);

        return ResponseEntity.ok(response);
    }
}
