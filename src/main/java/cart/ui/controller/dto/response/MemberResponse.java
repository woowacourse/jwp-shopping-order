package cart.ui.controller.dto.response;

import cart.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "멤버 응답")
public class MemberResponse {

    @Schema(description = "멤버 ID", example = "1")
    private Long id;

    @Schema(description = "멤버 이메일", example = "a@a.com")
    private String email;

    @Schema(description = "멤버 비밀번호", example = "password1")
    private String password;

    @Schema(description = "멤버 포인트", example = "1000")
    private int point;

    private MemberResponse() {
    }

    private MemberResponse(Long id, String email, String password, int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getPassword(), member.getPoint());
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

    public int getPoint() {
        return point;
    }
}
