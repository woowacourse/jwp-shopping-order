package cart.domain.member;

import java.util.Objects;

public class Member {

    private Long id;
    private final Email email;
    private final Password password;

    public Member(final Long id, final String email, final String password) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password);
    }

    public Member(final String email, final String password) {
        this.email = new Email(email);
        this.password = new Password(password);
    }

    public boolean checkPassword(final String password) {
        return this.password.isPassed(password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.getEmail();
    }

    public String getPassword() {
        return password.getPassword();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
