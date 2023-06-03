package cart.common.dto;

public class MemberRequest {
    private String email;
    private String password;
    private Integer point;

    public MemberRequest(String email, String password, Integer point) {
        this.email = email;
        this.password = password;
        this.point = point;
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
