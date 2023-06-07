package cart.fixture;

import cart.domain.member.Rank;
import cart.entity.MemberEntity;

public class MemberFixture {

    public static MemberEntity ako = generateAko(null);
    public static MemberEntity ddoring = generateDDoring(null);

    public static MemberEntity generateAko(Long id) {
        return new MemberEntity(id, "ako@wooteco.com", "Abcd1234@", Rank.GOLD.name(), 200_000);
    }

    public static MemberEntity generateDDoring(Long id) {
        return new MemberEntity(id, "ddoring@wooteco.com", "Abcd1234@", Rank.DIAMOND.name(), 500_000);
    }

}
