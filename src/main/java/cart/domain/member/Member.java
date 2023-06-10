package cart.domain.member;

import java.util.Objects;

public class Member {
    private final Long id;
    private final Email email;
    private final Password password;
    private final Nickname nickname;

    public Member(final Long id, final Email email, final Password password, final Nickname nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void checkPassword(String password) {
        Password otherPassword = new Password(password);
        this.password.checkPassword(otherPassword);
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

    public String getNickname() {
        return nickname.getValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
