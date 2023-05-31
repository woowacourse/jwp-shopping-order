package cart.fixture;

import cart.domain.Member;
import cart.domain.Rank;

public class MemberFixture {

    public static Member ako = generate(null);

    public static Member generate(Long id) {
        return new Member(id, "ako@wooteco.com", "Abcd1234@", Rank.GOLD);
    }
}
