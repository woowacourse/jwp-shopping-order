package cart.domain;

public class Member {

    private Long id;
    private String email;
    private String password;
    private String nickname;

    public Member(final String email, final String password, final String nickname) {
        this(null, email, password, nickname);
    }

    public Member(final Long id, final String email, final String password, final String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
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

    public String getNickname() {
        return nickname;
    }
}
