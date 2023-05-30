package cart.ui.member;

import cart.application.MemberService;
import cart.application.dto.MemberDto;
import cart.ui.member.dto.request.MemberJoinRequest;
import cart.ui.member.dto.request.MemberLoginRequest;
import cart.ui.member.dto.response.MemberLoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
public class MemberRestController {
    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public ResponseEntity<Void> joinMember(@RequestBody MemberJoinRequest request) {
        MemberDto memberDto = new MemberDto(request.getName(), request.getPassword());

        memberService.join(memberDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> loginMember(@RequestBody MemberLoginRequest request) {
        MemberDto memberDto = new MemberDto(request.getName(), request.getPassword());
        String encryptedPassword = memberService.login(memberDto);
        String token = Base64Utils.encodeToUrlSafeString(encryptedPassword.getBytes());

        return ResponseEntity.ok(new MemberLoginResponse(token));
    }
}
