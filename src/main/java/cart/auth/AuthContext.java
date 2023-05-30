package cart.auth;

import cart.domain.member.Member;
import org.springframework.stereotype.Component;

@Component
public class AuthContext {

    private final ThreadLocal<Member> local = new ThreadLocal<>();

    public void set(final Member member) {
        local.set(member);
    }

    public Member get() {
        return local.get();
    }

    public void clear() {
        local.remove();
    }
}
