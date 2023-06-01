package cart.fixture;

import cart.domain.member.Member;
import cart.domain.member.Rank;

public class MemberFixture {

    public static Member ako = generateAko(null);
    public static Member ddoring = generateDDoring(null);

    public static Member generateAko(Long id) {
        return new Member(id, "ako@wooteco.com", "Abcd1234@", Rank.GOLD, 200_000);
    }

    public static Member generateDDoring(Long id) {
        return new Member(2L, "ddoring@wooteco.com", "Abcd1234@", Rank.DIAMOND, 500_000);
    }

}
