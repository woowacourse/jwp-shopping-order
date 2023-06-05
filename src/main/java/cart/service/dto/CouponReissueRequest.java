package cart.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CouponReissueRequest {

    @NotNull(message = "아이디를 입력해주세요.")
    @Schema(description = "쿠폰 아이디", example = "1")
    private final Long id;
    @NotBlank(message = "이메일을 입력해주세요.")
    @Schema(description = "사용자 이메일", example = "a@a.com")
    private final String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Schema(description = "사용자 비밀번호", example = "1234")
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
