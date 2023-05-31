package shop.domain.event;

public class MemberJoinedEvent {
    private final Long memberId;

    public MemberJoinedEvent(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
