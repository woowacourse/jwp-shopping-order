package cart.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CouponReissueRequest {

    @NotNull(message = "아이디를 입력해주세요.")
    private final Long id;
    @NotBlank(message = "이메일을 입력해주세요.")
    private final String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private final String password;

    public CouponReissueRequest(final Long id, final String email, final String password) {
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
}
