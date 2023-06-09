package cart.member.presentation.request;

import cart.member.application.dto.MemberAddDto;

public class MemberAddRequest {
    private String email;
    private String password;
    private Integer point;

    public MemberAddRequest(String email, String password, Integer point) {
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public MemberAddDto toDto() {
        return new MemberAddDto(email, password, point);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getPoint() {
        return point;
    }
}
