package cart.dto.request;

import cart.domain.Member;

public class MemberCreateRequest {
    private final String email;
    private final String password;

    public MemberCreateRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public static MemberCreateRequest from(final Member member) {
        return new MemberCreateRequest(member.getEmail(), member.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
