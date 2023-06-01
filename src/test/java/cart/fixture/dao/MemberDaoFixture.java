package cart.fixture.dao;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.domain.vo.Money;
import cart.dao.entity.MemberEntity;
import cart.exception.MemberException;

import static cart.fixture.entity.MemberEntityFixture.회원_엔티티;

public class MemberDaoFixture {

    private final MemberDao memberDao;

    public MemberDaoFixture(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member 회원을_등록한다(String 이메일, String 패스워드, String 금액, String 포인트) {
        MemberEntity 회원_엔티티 = 회원_엔티티(이메일, 패스워드, 금액, 포인트);
        Long 회원_식별자값 = memberDao.insertMember(회원_엔티티);

        return new Member(
                회원_식별자값,
                회원_엔티티.getEmail(),
                회원_엔티티.getPassword(),
                Money.from(회원_엔티티.getMoney()),
                Money.from(회원_엔티티.getPoint())
        );
    }

    public Member 회원을_조회한다(Long 회원_식별자값) {
        return memberDao.getByMemberId(회원_식별자값)
                .orElseThrow(MemberException.NotExist::new)
                .toDomain();
    }
}
