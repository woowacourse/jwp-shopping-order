package cart.repository;

import cart.dao.CouponDao;
import cart.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Import({CouponJdbcRepository.class, CouponDao.class})
class CouponJdbcRepositoryTest {

    @Autowired
    private CouponJdbcRepository couponJdbcRepository;

    @Test
    void 쿠폰을_발급한다() {
        // given
        final Member member = new Member(1L, "a@a.com", "1234");

        // when
        final Long saveId = couponJdbcRepository.issue(member, 1L);

        // then
        assertThat(saveId).isNotNull();
    }

    @Test
    void 쿠폰_상태를_변경한다 () {
        // when, then
        assertThatCode(() -> couponJdbcRepository.changeStatus(1L, 1L))
                .doesNotThrowAnyException();
    }
}
