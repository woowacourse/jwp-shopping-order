package cart.controller;

import cart.application.PointService;
import cart.domain.member.Member;
import cart.dto.response.PointHistoryResponse;
import cart.dto.response.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointController {

    private final PointService pointService;

    public PointController(final PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/points")
    public ResponseEntity<PointResponse> findPointOfMember(final Member member) {
        final PointResponse pointByMember = pointService.findPointByMember(member);

        return ResponseEntity.ok(pointByMember);
    }

    @GetMapping("orders/{orderId}/point")
    public ResponseEntity<PointHistoryResponse> findPointOfMember(@PathVariable final Long orderId) {
        final PointHistoryResponse pointHistory = pointService.findPointHistory(orderId);

        return ResponseEntity.ok(pointHistory);
    }
}
