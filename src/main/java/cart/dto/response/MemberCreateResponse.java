package cart.dto.response;

import cart.domain.Member;

public class MemberCreateResponse {

    private final Long id;
    private final String email;
    private final String password;

    public MemberCreateResponse(final Long id, final String email, final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static MemberCreateResponse from(final Member member) {
        return new MemberCreateResponse(member.getId(), member.getEmail(), member.getPassword());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
