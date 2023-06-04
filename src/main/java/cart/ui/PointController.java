package cart.ui;

import cart.domain.Member;
import cart.domain.Point;
import cart.dto.MemberPointResponse;
import cart.dto.SavingPointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "포인트 관련 API", description = "포인트 정보를 관리하는 API 입니다.")
public class PointController {

    @GetMapping("/points")
    @Operation(summary = "멤버별 포인트 조회")
    @SecurityRequirement(name = "basic")
    public ResponseEntity<MemberPointResponse> findPointOfMember(
            Member member
    ) {
        return ResponseEntity.ok(new MemberPointResponse(member.getPoint()));
    }

    @GetMapping("/saving-point")
    @Operation(summary = "가격별 적립되는 포인트 조회")
    public ResponseEntity<SavingPointResponse> calculatePointByPayment(
            @Parameter(description = "결제할 가격", example = "45000") @RequestParam Integer totalPrice
    ) {
        return ResponseEntity.ok(new SavingPointResponse(Point.fromPayment(totalPrice)));
    }
}
