package cart.domain;

import cart.exception.AuthenticationException;

import java.util.Objects;

public class Member {
    private Long id;
    private String email;
    private String password;
    private String nickname;

    public Member(final Long id, final String email, final String password, final String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    // TODO: 6/3/23 안에서 예외 터르리기 
    public void checkPassword(String password) {
        if (this.password.equals(password)) {
            throw new AuthenticationException.LoginFail("로그인 정보가 잘못되었습니다.");
        }
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

    public String getNickname() {
        return nickname;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Member member = (Member) o;
        return Objects.equals(id, member.id)
                && Objects.equals(email, member.email)
                && Objects.equals(password, member.password)
                && Objects.equals(nickname, member.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, nickname);
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
