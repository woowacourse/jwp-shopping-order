package cart.domain.member.dto;

import cart.domain.member.Member;

public class MemberWithId {
    private final Long id;
    private final Member member;

    public MemberWithId(final Long id, final Member member) {
        this.id = id;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }
}
