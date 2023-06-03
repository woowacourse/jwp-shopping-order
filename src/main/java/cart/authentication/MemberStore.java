package cart.authentication;

import cart.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberStore {

    private final ThreadLocal<Member> localMember = new ThreadLocal<>();

    public void set(Member member) {
        localMember.set(member);
    }

    public Member get() {
        return localMember.get();
    }

    public void remove() {
        localMember.remove();
    }
}
