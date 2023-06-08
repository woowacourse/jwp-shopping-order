package cart.domain.event;

public class JoinMemberCouponEvent {

    private final Long memberId;

    public JoinMemberCouponEvent(final Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
