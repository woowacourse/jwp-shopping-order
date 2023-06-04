package cart.ui;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import cart.application.MemberService;
import cart.dto.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Controller
@RequestMapping("/auth")
public class AuthApiController {

    private final MemberService memberService;

    public AuthApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "로그인", description = "로그인을 한다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "로그인 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "로그인 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        )
    })
    @PostMapping("/login")
    public ResponseEntity<Void> addCartItems(HttpServletRequest httpServletRequest) {
        memberService.logIn(httpServletRequest);
        return ResponseEntity.ok().build();
    }
}
