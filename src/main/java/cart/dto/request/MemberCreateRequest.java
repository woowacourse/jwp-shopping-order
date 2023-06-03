package cart.dto.request;

import cart.domain.Member;

import javax.validation.constraints.NotNull;

public class MemberCreateRequest {

    @NotNull(message = "이메일을 입력해 주세요. 입력값 : ${validatedValue}")
    private final String email;

    @NotNull(message = "비밀번호를 입력해 주세요. 입력값 : ${validatedValue}")
    private final String password;

    public MemberCreateRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public static MemberCreateRequest from(final Member member) {
        return new MemberCreateRequest(member.getEmail(), member.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
