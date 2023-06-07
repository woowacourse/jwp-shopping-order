package cart.domain;

import cart.exception.AuthenticationException;

import java.util.Objects;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final Point point;

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = new Point(0L);
    }

    public Member(Long id, String email, String password, Point point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
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

    public Point getPoint() {
        return point;
    }

    public void checkPassword(String password) {
        if (!this.password.equals(password)) {
            throw new AuthenticationException();
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", point=" + point +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
