package cart.event;

import cart.domain.member.Member;

public class MemberUpdateEvent {

    private final Member member;

    private MemberUpdateEvent(Member member) {
        this.member = member;
    }

    public static MemberUpdateEvent from(Member member) {
        return new MemberUpdateEvent(member);
    }

    public Member getMember() {
        return member;
    }
}
