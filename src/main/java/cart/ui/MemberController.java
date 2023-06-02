package cart.ui;

import cart.domain.Member;
import cart.dto.response.MemberResponse;
import cart.mapper.MemberMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    @GetMapping
    public ResponseEntity<MemberResponse> aboutMe(Member member) {
        MemberResponse response = MemberMapper.toResponse(member);
        return ResponseEntity.ok().body(response);
    }
}
