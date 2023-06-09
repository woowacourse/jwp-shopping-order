package cart.domain.member;

import java.util.Objects;

public class Member {

    private final Long id;
    private final MemberName name;
    private final MemberEmail email;
    private final MemberPassword password;

    public Member(String name, String email, String password) {
        this(null, name, email, password);
    }

    public Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = MemberName.from(name);
        this.email = MemberEmail.from(email);
        this.password = MemberPassword.from(password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getMemberName();
    }

    public String getEmail() {
        return email.getMemberEmail();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isEqualId(final Long memberId) {
        return memberId == this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
