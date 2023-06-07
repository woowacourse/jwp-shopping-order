package cart.dao;

import cart.config.DaoTestConfig;
import cart.domain.vo.Money;
import cart.dao.entity.MemberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static cart.fixture.entity.MemberEntityFixture.회원_엔티티;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberDaoTest extends DaoTestConfig {

    MemberDao memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void 회원_보유_금액을_차감한다() {
        // given
        Long 회원_식별자값 = memberDao.insertMember(회원_엔티티("a@a.com", "1234", "1000", "1000"));

        // when
        memberDao.updateMoney(회원_식별자값, Money.from(0));

        Optional<MemberEntity> 아마도_회원 = memberDao.getByMemberId(회원_식별자값);

        // then
        assertAll(
                () -> assertThat(아마도_회원).isPresent(),
                () -> assertThat(아마도_회원.get().getMoney())
                        .isEqualByComparingTo(BigDecimal.ZERO)
        );
    }

    @Test
    void 회원_보유_포인트를_차감한다() {
        // given
        Long 회원_식별자값 = memberDao.insertMember(회원_엔티티("a@a.com", "1234", "1000", "1000"));

        // when
        memberDao.updatePoint(회원_식별자값, Money.from(0));

        Optional<MemberEntity> 아마도_회원 = memberDao.getByMemberId(회원_식별자값);

        // then
        assertAll(
                () -> assertThat(아마도_회원).isPresent(),
                () -> assertThat(아마도_회원.get().getPoint())
                        .isEqualByComparingTo(BigDecimal.ZERO)
        );
    }

    @Test
    void 회원_포인트를_적립한다() {
        // given
        Long 회원_식별자값 = memberDao.insertMember(회원_엔티티("a@a.com", "1234", "1000", "1000"));

        // when
        memberDao.updatePoint(회원_식별자값, Money.from(2000));

        Optional<MemberEntity> 아마도_회원 = memberDao.getByMemberId(회원_식별자값);

        // then
        assertAll(
                () -> assertThat(아마도_회원).isPresent(),
                () -> assertThat(아마도_회원.get().getPoint())
                        .isEqualByComparingTo(BigDecimal.valueOf(2000))
        );
    }
}
