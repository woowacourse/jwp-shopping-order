package cart.domain;

import java.util.Objects;

public class Member {
    private Long id;
    private String email;
    private String password;
    private MemberGrade grade;

    public Member(final Long id, final String email, final String password, final MemberGrade grade) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.grade = grade;
    }

    public Member(Long id, String email, String password) {
        this(id, email, password, MemberGrade.NOTHING);
    }

    public Member(final String email, final String password) {
        this(null, email, password, MemberGrade.NOTHING);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        final Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
