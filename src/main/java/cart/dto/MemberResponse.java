package cart.dto;

import cart.domain.Member;
import cart.domain.price.discount.grade.Grade;

public class MemberResponse {
    private final Long id;
    private final String email;
    private final String password;
    private final String grade;

    private MemberResponse(Long id, String email, String password, String grade) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.grade = grade;
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

    public static MemberResponse of(Member member) {
        final String grade = Grade.findGradeByGradeValue(member.getGrade()).name();
        return new MemberResponse(member.getId(), member.getEmail(), member.getPassword(), grade);
    }
}
