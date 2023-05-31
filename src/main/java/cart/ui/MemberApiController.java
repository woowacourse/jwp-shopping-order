package cart.ui;

import cart.application.PointService;
import cart.domain.Member;
import cart.dto.MemberForOrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class MemberApiController {

    private final PointService pointService;

    public MemberApiController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public ResponseEntity<MemberForOrderResponse> showMemberForOrder(Member member) {
        return ResponseEntity.ok(new MemberForOrderResponse(member.getEmail(), pointService.findByMember(member), pointService.findEarnRateByMember(member)));
    }
}
