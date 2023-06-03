package cart.authentication;

import cart.domain.Member;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class MemberStore {

    private Member member;

    public void set(Member member) {
        this.member = member;
    }

    public Member get() {
        return member;
    }
}
