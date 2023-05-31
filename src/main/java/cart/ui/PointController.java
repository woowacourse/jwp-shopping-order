package cart.ui;

import cart.domain.Member;
import cart.dto.MemberPointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointController {

    @GetMapping("/points")
    public ResponseEntity<MemberPointResponse> findPointOfMember(Member member) {
        return ResponseEntity.ok(new MemberPointResponse(member.getPoint()));
    }
}
