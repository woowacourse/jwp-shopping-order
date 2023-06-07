package cart.dto.member;

import cart.domain.member.Member;

public class MemberResponse {

    private final long memberId;
    private final String email;
    private final String password;

    public MemberResponse(final long memberId, final String email, final String password) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
    }

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getPassword());
    }

    public long getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
