package cart.domain.member;

import cart.exception.member.MemberNotValidException;
import java.util.Objects;

public class Member {

    private final Long id;
    private final String email;
    private final String password;

    public Member(final String email, final String password) {
        this(null, email, password);
    }

    public Member(final Long id, final String email, final String password) {
        validateEmail(email);
        this.id = id;
        this.email = email;
        this.password = password;
    }

    private void validateEmail(final String email) {
        if (email == null || email.isBlank()) {
            throw new MemberNotValidException("이메일은 공백일 수 없습니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
}
