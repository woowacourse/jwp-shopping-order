package cart.dto.member;

public class MemberResponse {

    private String email;
    private Long point;
    private Double earnRate;

    public MemberResponse(String email, Long point, Double earnRate) {
        this.email = email;
        this.point = point;
        this.earnRate = earnRate;
    }

    public String getEmail() {
        return email;
    }

    public Long getPoint() {
        return point;
    }

    public Double getEarnRate() {
        return earnRate;
    }
}
