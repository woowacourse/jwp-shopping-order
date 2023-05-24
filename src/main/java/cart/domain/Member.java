package cart.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class Member {

    @NotNull
    private Long id;

    @Email
    private String email;

    @NotNull
    private String password;

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
