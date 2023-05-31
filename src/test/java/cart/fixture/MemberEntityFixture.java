package cart.fixture;

import cart.entity.MemberEntity;

import java.math.BigDecimal;

public abstract class MemberEntityFixture {

    public static MemberEntity 회원_엔티티(String 이메일, String 패스워드, String 금액, String 포인트) {
        return new MemberEntity(이메일, 패스워드, new BigDecimal(금액), new BigDecimal(포인트));
    }
}
