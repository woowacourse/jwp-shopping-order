package cart.domain.member;

import java.util.Objects;

public class Member {
    private Long id;
    private Email email;
    private Password password;

    public Member(String email, String password) {
        this(null, email, password);
    }

    public Member(Long id, String email, String password) {
        this(id, new Email(email), new Password(password));
    }

    public Member(Long id, Email email, Password password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.isCorrect(password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.nonNull(id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
