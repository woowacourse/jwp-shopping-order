package cart.ui;

import cart.application.PointService;
import cart.domain.Member;
import cart.dto.MemberForOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "member", description = "멤버 API")
@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/users")
public class MemberApiController {

    private final PointService pointService;

    public MemberApiController(PointService pointService) {
        this.pointService = pointService;
    }

    @Operation(
            summary = "주문에 필요한 정보 조회",
            description = "주문에 필요한 멤버의 정보를 조회한다.",
            responses = {
                    @ApiResponse(description = "멤버 정보 조회 성공", responseCode = "200"),
                    @ApiResponse(description = "인증에러", responseCode = "401")
            }
    )
    @GetMapping
    public ResponseEntity<MemberForOrderResponse> showMemberForOrder(@Parameter(name = "member", description = "인증된 멤버", required = true, hidden = true) Member member) {
        return ResponseEntity.ok(new MemberForOrderResponse(member.getEmail(), pointService.findByMember(member), pointService.findEarnRateByMember(member)));
    }
}
