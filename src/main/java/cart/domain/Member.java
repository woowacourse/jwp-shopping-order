package cart.domain;

import java.util.Objects;

public class Member {
    private final Long id;
    private final String email;
    private final String password;

    public Member(final String email, final String password) {
        this(null, email, password);
    }

    public Member(final Long id, final String email, final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }

    public boolean matchMemberByInfo(final Member other) {
        return this.password.equals(other.password)
                && this.email.equals(other.email);
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
        return Objects.equals(getId(), member.getId()) && Objects.equals(getEmail(), member.getEmail())
                && Objects.equals(getPassword(), member.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getPassword());
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
