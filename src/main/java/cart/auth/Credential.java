package cart.auth;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.lang.Nullable;

@Hidden
public class Credential {

    private final Long memberId;
    private final String email;
    private final String password;

    public Credential(final String email, final String password) {
        this(null, email, password);
    }

    public Credential(final Long memberId, final String email, final String password) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
    }

    public boolean isNotSamePassword(final Credential credential) {
        return !this.password.equals(credential.getPassword());
    }

    public String getPassword() {
        return password;
    }

    @Nullable
    public Long getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }
}
