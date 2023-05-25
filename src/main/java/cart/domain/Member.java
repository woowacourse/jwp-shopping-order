package cart.domain;

import java.util.Objects;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final Integer grade;

    public Member(Long id, String email, String password, Integer grade) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.grade = grade;
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

    public Integer getGrade() {
        return grade;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
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
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
