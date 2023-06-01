package cart.dto.request;

import javax.validation.constraints.NotNull;

public class MemberCreateRequest {

    @NotNull(message = "이메일은 필수 입력 사항입니다.")
    private String email;
    @NotNull(message = "비밀번호는 필수 입력 사항입니다.")
    private String password;

    public MemberCreateRequest() {
    }

    public MemberCreateRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
