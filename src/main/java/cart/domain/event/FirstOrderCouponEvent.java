package cart.domain.event;

public class FirstOrderCouponEvent {

    private final Long memberId;

    public FirstOrderCouponEvent(final Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
