package cart.fixture;

import cart.domain.member.Member;
import cart.entity.MemberEntity;

@SuppressWarnings("NonAsciiCharacters")
public class MemberFixture {

    public static final Member 사용자1 = new Member("pizza1@pizza.com", "password");
    public static final Member 사용자2 = new Member("pizza2@pizza.com", "password");

    public static final MemberEntity 사용자1_엔티티 = new MemberEntity("pizza1@pizza.com", "password");
    public static final MemberEntity 사용자2_엔티티 = new MemberEntity("pizza2@pizza.com", "password");
}
