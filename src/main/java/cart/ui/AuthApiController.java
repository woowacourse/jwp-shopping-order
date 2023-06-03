package cart.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import cart.domain.Member;
import cart.dto.request.CartItemRequest;
import cart.dto.request.LogInRequest;
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
    public ResponseEntity<Void> addCartItems(Member member) {
        memberService.logIn(member);
        return ResponseEntity.ok().build();
    }
}
