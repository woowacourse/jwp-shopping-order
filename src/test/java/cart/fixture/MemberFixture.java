package cart.fixture;

import cart.domain.member.Member;

public class MemberFixture {

    public static Member createMember() {
        return new Member(1L, "a@a.com", "1234");
    }

    public static Member createMember2() {
        return new Member(2L, "b@b.com", "1234");
    }
}
