package cart.fixture;

import cart.domain.member.Member;
import cart.domain.member.Rank;

public class MemberFixture {

    public static Member ako = generate(null);

    public static Member generate(Long id) {
        return new Member(id, "ako@wooteco.com", "Abcd1234@", Rank.GOLD, 200_000);
    }
}
