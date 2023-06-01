package cart.fixture;

import cart.domain.Member;
import cart.persistence.entity.MemberEntity;

public class MemberFixture {

    public static MemberEntity 멤버_test1_엔티티 = new MemberEntity("test1@test.com", "password1", "test1");
    public static Member 멤버_test1_도메인 = new Member("test1@test.com", "password1", "test1");

    public static MemberEntity 멤버_test2_엔티티 = new MemberEntity("test2@test.com", "password2", "test2");
}
