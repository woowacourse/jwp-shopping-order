package cart.application.dto.member;

import cart.domain.Member;

public class FindProfileResponse {

    private long id;
    private String email;
    private String nickname;

    public FindProfileResponse(final long id, final String email, final String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public static FindProfileResponse from(final Member member) {
        return new FindProfileResponse(member.getId(), member.getEmail(), member.getNickname());
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
