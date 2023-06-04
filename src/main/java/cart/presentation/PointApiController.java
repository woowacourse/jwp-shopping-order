package cart.presentation;

import cart.domain.Member;
import cart.dto.response.PointResponse;
import cart.service.PointService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/points")
public class PointApiController {

    private final PointService pointService;

    @GetMapping
    public ResponseEntity<PointResponse> getUserPoint(final Member member) {
        PointResponse pointResponse = pointService.getUserPoint(member);

        return ResponseEntity.ok(pointResponse);
    }
}
