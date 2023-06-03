package cart.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cart.application.MemberService;
import cart.domain.Member;
import cart.dto.response.ExceptionResponse;
import cart.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Controller
@RequestMapping("/members")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "회원 정보 조회 성공"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "회원 정보 조회 실패 - 권한이 없음",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        )
    })
    @GetMapping("/profile")
    public ResponseEntity<MemberResponse> getProfile(Member member) {
        return ResponseEntity.ok().body(memberService.findProfile(member));
    }
}
