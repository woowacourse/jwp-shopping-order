package cart.ui.api;

import cart.application.QueryMemberService;
import cart.application.response.QueryMemberResponse;
import cart.domain.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RestController
public class MemberApiController {

    private final QueryMemberService queryMemberService;

    public MemberApiController(QueryMemberService queryMemberService) {
        this.queryMemberService = queryMemberService;
    }

    @GetMapping
    public ResponseEntity<QueryMemberResponse> showMember(Member member) {
        QueryMemberResponse response = queryMemberService.findByMemberId(member.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
