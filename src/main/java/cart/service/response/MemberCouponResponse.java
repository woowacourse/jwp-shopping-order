package cart.service.response;

public class MemberCouponResponse {

    private final Long memberCouponId;
    private final String name;

    private MemberCouponResponse() {
        this(null, null);
    }

    public MemberCouponResponse(final Long memberCouponId, final String name) {
        this.memberCouponId = memberCouponId;
        this.name = name;
    }

    public Long getMemberCouponId() {
        return memberCouponId;
    }

    public String getName() {
        return name;
    }
}
