package cart.dto;

import cart.domain.Member;

public final class MemberForOrderResponse {

    private String email;
    private int point;
    private int earnRate;

    private MemberForOrderResponse(String email, int point, int earnRate) {
        this.email = email;
        this.point = point;
        this.earnRate = earnRate;
    }

    public static MemberForOrderResponse of(Member member, int point, double earnRate) {
        return new MemberForOrderResponse(member.getEmail(), point, (int) (earnRate * 100));
    }

    public String getEmail() {
        return email;
    }

    public int getPoint() {
        return point;
    }

    public double getEarnRate() {
        return earnRate;
    }
}
