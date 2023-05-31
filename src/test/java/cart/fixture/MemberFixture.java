package cart.fixture;

import cart.domain.Member;
import cart.domain.vo.Money;

public abstract class MemberFixture {

    public static Member 회원(String 이메일, String 비밀번호, Money 금액, Money 포인트) {
        return new Member(이메일, 비밀번호, 금액, 포인트);
    }
}
