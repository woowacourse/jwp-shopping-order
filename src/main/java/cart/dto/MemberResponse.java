package cart.dto;

import cart.domain.Member;

public class MemberResponse {
    private final String email;
    private final String password;

    public MemberResponse(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getEmail(), member.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
