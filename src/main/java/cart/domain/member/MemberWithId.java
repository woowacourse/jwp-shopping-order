package cart.domain.member;

public class MemberWithId {

    private final Long memberId;
    private final Member member;

    public MemberWithId(final Long memberId, final Member member) {
        this.memberId = memberId;
        this.member = member;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Member getMember() {
        return member;
    }

    public boolean isSameName(final String memberName) {
        return member.isSameName(memberName);
    }
}
