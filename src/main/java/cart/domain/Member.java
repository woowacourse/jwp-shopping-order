package cart.domain;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final String nickname;

    public Member(Long id, String email, String password, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
