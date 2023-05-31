package cart.ui.controller;

import cart.application.MemberService;
import cart.domain.member.Member;
import cart.ui.controller.dto.response.MemberPointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "basic")
@Tag(name = "멤버", description = "멤버 API")
@RequestMapping("/members")
@RestController
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "멤버 포인트 조회", description = "멤버 포인트를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 포인트 조회 성공"),
            @ApiResponse(responseCode = "404", description = "등록되지 않은 데이터(멤버) 요청")
    })
    @GetMapping("/point")
    public ResponseEntity<MemberPointResponse> getMemberPoint(Member member) {
        MemberPointResponse response = memberService.getMemberPoint(member);
        return ResponseEntity.ok(response);
    }
}
