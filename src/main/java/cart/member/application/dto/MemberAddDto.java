package cart.member.application.dto;

public class MemberAddDto {
    private String email;
    private String password;
    private Integer point;

    public MemberAddDto(String email, String password, Integer point) {
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
