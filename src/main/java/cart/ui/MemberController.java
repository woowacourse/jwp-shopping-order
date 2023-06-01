package cart.ui;

import cart.domain.Member;
import cart.domain.Point;
import cart.dto.response.PointResponse;
import cart.dto.response.Response;
import cart.dto.response.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RestController
public class MemberController {

    @GetMapping("/point")
    public ResponseEntity<Response> inquiryPoint(Member member) {
        Point point = member.getPoint();
        PointResponse pointResponse = new PointResponse(member.getId(), point.getAmount());
        return ResponseEntity.ok()
                .body(new ResultResponse<>("포인트가 조회되었습니다.", pointResponse));
    }
}
