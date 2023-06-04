package cart.dto;

import io.swagger.annotations.ApiModelProperty;

public class User {
    @ApiModelProperty(hidden = true)
    private final Long memberId;
    @ApiModelProperty(hidden = true)
    private final String email;

    public User(Long memberId, String email) {
        this.memberId = memberId;
        this.email = email;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }
}
