package cart.dto.response;

import cart.domain.Member;

public class MemberResponse {

    private final Long id;
    private final String email;
    private final String password;
    private final String grade;

    public MemberResponse(final Long id, final String email, final String password, final String grade) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.grade = grade;
    }

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getPassword(), member.getGradeName());
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

    public String getGrade() {
        return grade;
    }
}
