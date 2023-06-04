package cart.ui;

import cart.domain.Member;
import cart.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberApiController {

    @GetMapping("/profile")
    public ResponseEntity<MemberResponse> myMemberInfo(Member member) {
        // TODO: 5/30/23 서비스로 뺴기
        MemberResponse memberResponse = new MemberResponse(member.getId(), member.getEmail(), member.getNickname());

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberResponse);
    }
}
