package cart.fixture;

import cart.domain.member.Member;

import static cart.fixture.CartFixture.createCart;

public class MemberFixture {

    public static Member createMember() {
        return new Member(1L, "a@a.com", "1234");
    }

    public static Member createMemberWithCart() {
        Member member = new Member(1L, "a@a.com", "1234");
        member.initCart(createCart());
        return member;
    }

    public static Member createMember2() {
        return new Member(2L, "b@b.com", "1234");
    }
}
