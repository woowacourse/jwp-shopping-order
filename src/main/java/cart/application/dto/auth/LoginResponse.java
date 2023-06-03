package cart.application.dto.auth;

import cart.domain.Member;

public class LoginResponse {

    private long id;
    private String email;
    private String nickname;

    public LoginResponse(final long id, final String email, final String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public static LoginResponse from(final Member member) {
        return new LoginResponse(member.getId(), member.getEmail(), member.getNickname());
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }
}
