package cart.application.service.member;

import cart.domain.member.Member;
import cart.ui.member.dto.MemberRequest;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member toMember(MemberRequest memberRequest) {
        return new Member(null, memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword());
    }
}
