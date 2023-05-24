package cart.domain;

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
}
