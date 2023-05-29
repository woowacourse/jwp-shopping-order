package cart.ui.controller.dto.response;

import cart.domain.member.Member;

public class MemberResponse {

    private final Long id;
    private final String email;
    private final String password;
    private final int point;

    private MemberResponse(Long id, String email, String password, int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getPassword(), member.getPoint());
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

    public int getPoint() {
        return point;
    }
}
