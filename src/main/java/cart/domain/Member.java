package cart.domain;

import java.util.Objects;

public class Member {
    private Long id;
    private String email;
    private String password;

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        if (Objects.isNull(id) || Objects.isNull(member.id)) {
            return Objects.equals(email, member.email);
        }
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        if (Objects.isNull(id)) {
            return Objects.hash(email);
        }
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
