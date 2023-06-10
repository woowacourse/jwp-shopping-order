package cart.fixture;

import cart.dao.entity.MemberEntity;
import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Nickname;
import cart.domain.member.Password;

public class MemberFixture {

    public static final MemberEntity 라잇_엔티티 = new MemberEntity(1L, "a@a.com", "1234", "라잇");
    public static final Member 라잇 = new Member(1L, new Email("a@a.com"), new Password("1234"), new Nickname("라잇"));
    public static final MemberEntity 엽토_엔티티 = new MemberEntity(2L, "b@b.com", "1234", "엽토");
    public static final Member 엽토 = new Member(2L, new Email("b@b.com"), new Password("1234"), new Nickname("엽토"));
    public static final MemberEntity 루카_엔티티 = new MemberEntity(3L, "c@c.com", "1234", "루카");
    public static final Member 루카 = new Member(3L, new Email("c@c.com"), new Password("1234"), new Nickname("루카"));

}
