package cart.ui;

import cart.domain.Member;
import cart.domain.Point;
import cart.dto.response.PointResponse;
import cart.dto.response.ShoppingOrderResponse;
import cart.dto.response.ShoppingOrderResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RestController
public class MemberApiController {

    @GetMapping("/point")
    public ResponseEntity<ShoppingOrderResponse> inquiryPoint(Member member) {
        Point point = member.getPoint();
        PointResponse pointResponse = new PointResponse(member.getId(), point.getAmount());
        return ResponseEntity.ok()
                .body(new ShoppingOrderResultResponse<>("포인트가 조회되었습니다.", pointResponse));
    }
}
