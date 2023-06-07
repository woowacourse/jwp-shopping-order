package cart.dto;

import cart.domain.Member;
import cart.domain.MemberGrade;

public final class MemberResponse {
    private final Long id;
    private final String email;
    private final String password;
    private final MemberGrade grade;

    public MemberResponse(final Long id, final String email, final String password, final MemberGrade grade) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.grade = grade;
    }

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getPassword(), member.getGrade());
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

    public MemberGrade getGrade() {
        return grade;
    }
}
