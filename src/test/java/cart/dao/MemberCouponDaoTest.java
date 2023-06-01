package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.dto.MemberCouponDto;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class MemberCouponDaoTest {

    @Autowired
    private DataSource dataSource;
    private MemberCouponDao memberCouponDao;

    @BeforeEach
    void setUp() {
        memberCouponDao = new MemberCouponDao(dataSource);
        new JdbcTemplate(dataSource).update("SET REFERENTIAL_INTEGRITY FALSE");
    }

    @DisplayName("memberId 기준으로 조회하는 기능 테스트")
    @Test
    void findByMemberId() {
        final MemberCouponDto memberCouponDto = new MemberCouponDto(null, 2L, 3L);
        final Long memberCouponId = memberCouponDao.insert(memberCouponDto);

        final List<MemberCouponDto> memberCouponDtos = memberCouponDao.findByMemberId(memberCouponDto.getMemberId());

        assertThat(memberCouponDtos)
                .usingRecursiveComparison()
                .isEqualTo(List.of(new MemberCouponDto(memberCouponId, 2L, 3L)));
    }

    @Nested
    class findById {

        @DisplayName("실제 존재하는 Id인 경우")
        @Test
        void isPresent() {
            final MemberCouponDto memberCouponDto = new MemberCouponDto(null, 2L, 3L);
            final Long memberCouponId = memberCouponDao.insert(memberCouponDto);

            final Optional<MemberCouponDto> coupon = memberCouponDao.findById(memberCouponId);

            assertThat(coupon).isPresent();
            assertThat(coupon.get())
                    .extracting(MemberCouponDto::getId, MemberCouponDto::getMemberId, MemberCouponDto::getCouponId)
                    .containsExactly(memberCouponId, 2L, 3L);
        }

        @DisplayName("존재하지 않는 Id인 경우")
        @Test
        void isEmpty() {
            final Optional<MemberCouponDto> optionCouponDto = memberCouponDao.findById(1L);

            assertThat(optionCouponDto).isEmpty();
        }
    }

    @DisplayName("memberCouponDto를 저자하는 기능 테스트")
    @Test
    void insert() {
        final MemberCouponDto memberCouponDto = new MemberCouponDto(null, 2L, 3L);

        final Long id = memberCouponDao.insert(memberCouponDto);

        final Optional<MemberCouponDto> result = memberCouponDao.findById(id);
        assertThat(result).isPresent();
        assertThat(result.get())
                .extracting(MemberCouponDto::getId, MemberCouponDto::getMemberId, MemberCouponDto::getCouponId)
                .containsExactly(id, 2L, 3L);
    }

    @DisplayName("사용한 쿠폰을 soft delete하는 기능 테스트")
    @Test
    void updateUsedTrue() {
        final MemberCouponDto memberCouponDto = new MemberCouponDto(null, 2L, 3L);
        final Long memberCouponId = memberCouponDao.insert(memberCouponDto);

        memberCouponDao.updateUsedTrue(memberCouponId);

        final Optional<MemberCouponDto> optionCoupon = memberCouponDao.findById(memberCouponId);
        assertThat(optionCoupon).isEmpty();
    }
}
