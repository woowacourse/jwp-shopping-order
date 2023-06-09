package cart.ui;

import cart.application.PointService;
import cart.domain.Member.Member;
import cart.dto.response.OrderPointResponse;
import cart.dto.response.UserPointResponse;
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
    public ResponseEntity<UserPointResponse> showUserPoint(Member member){
        UserPointResponse userPointResponse = pointService.findByMemberId(member.getId());
        return ResponseEntity.ok(userPointResponse);
    }

    @GetMapping("orders/{id}/points")
    public ResponseEntity<OrderPointResponse> showOrderPoint(Member member, @PathVariable Long id){
        OrderPointResponse orderPointResponse = pointService.findSavedPointByOrderId(id);
        return ResponseEntity.ok(orderPointResponse);
    }
}
