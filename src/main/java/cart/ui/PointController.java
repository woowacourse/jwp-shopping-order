package cart.ui;

import cart.domain.Member;
import cart.domain.Point;
import cart.dto.MemberPointResponse;
import cart.dto.SavingPointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointController {

    @GetMapping("/points")
    public ResponseEntity<MemberPointResponse> findPointOfMember(Member member) {
        return ResponseEntity.ok(new MemberPointResponse(member.getPoint()));
    }

    @GetMapping("/saving-point")
    public ResponseEntity<SavingPointResponse> calculatePointByPayment(
            @RequestParam Integer totalPrice
    ) {
        return ResponseEntity.ok(new SavingPointResponse(Point.fromPayment(totalPrice)));
    }
}
