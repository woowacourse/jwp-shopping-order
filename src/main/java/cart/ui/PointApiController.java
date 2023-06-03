package cart.ui;

import cart.application.PointService;
import cart.domain.Member;
import cart.domain.Point;
import cart.dto.MemberPointResponse;
import cart.dto.OrderSavedPointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointApiController {

    private final PointService pointService;

    public PointApiController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/points")
    public ResponseEntity<MemberPointResponse> findPointOfMember(Member member) {
        Point point = pointService.findPointOfMember(member);
        return ResponseEntity.ok(MemberPointResponse.from(point));
    }

    @GetMapping("/orders/{id}/points")
    public ResponseEntity<OrderSavedPointResponse> findPointSavedByOrder(@PathVariable Long id) {
        Point savedPoint = pointService.findSavedPointByOrderId(id);
        return ResponseEntity.ok(OrderSavedPointResponse.from(savedPoint));
    }

}
