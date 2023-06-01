package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.dto.CouponDto;
import cart.dto.MemberCouponDto;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ActiveProfiles;

@JdbcTest
@ActiveProfiles("test")
class CouponRepositoryTest {

    private static final Member MEMBER_1 = new Member(1L, "a@a", "1234");
    private static final Member MEMBER_2 = new Member(2L, "b@b", "1234");
    private static final RowMapper<MemberCouponDto> memberCouponDtoRowMapper = (rs, rn) ->
            new MemberCouponDto(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("coupon_id")
            );

    private static final RowMapper<CouponDto> couponDtoRowMapper = (rs, rn) ->
            new CouponDto(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getDouble("discount_rate"),
                    rs.getInt("discount_price")
            );

    @Autowired
    private DataSource dataSource;
    private CouponRepository couponRepository;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        couponRepository = new CouponRepository(
                new CouponDao(dataSource),
                new MemberCouponDao(dataSource),
                new MemberDao(dataSource)
        );

        String couponSaveQuery = "INSERT INTO coupon (id, name, discount_rate, discount_price) VALUES(?, ?, ?, ?)";
        String memberSaveQuery = "INSERT INTO member (id, email, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(couponSaveQuery, 1L, "정액 할인 쿠폰", 0d, 5000);
        jdbcTemplate.update(couponSaveQuery, 2L, "할인율 쿠폰", 10d, 0);
        jdbcTemplate.update(memberSaveQuery, MEMBER_1.getId(), MEMBER_1.getEmail(), MEMBER_1.getPassword());
        jdbcTemplate.update(memberSaveQuery, MEMBER_2.getId(), MEMBER_2.getEmail(), MEMBER_2.getPassword());
    }

    @Test
    @DisplayName("사용자가 쿠폰을 추가하면, 현재 Coupon 테이블에 있는 쿠폰이 모두 추가된다.")
    void addCoupon() {
        couponRepository.saveCoupon(MEMBER_1);

        String findMemberCouponQuery = "SELECT * FROM member_coupon WHERE member_id = ?";
        String findCouponQuery = "SELECT * FROM coupon WHERE id = ?";
        List<MemberCouponDto> memberCouponDtos = jdbcTemplate.query(findMemberCouponQuery, memberCouponDtoRowMapper, MEMBER_1.getId());

        List<CouponDto> couponDtos = memberCouponDtos.stream()
                .map(memberCouponDto -> jdbcTemplate.queryForObject(findCouponQuery, couponDtoRowMapper,
                        memberCouponDto.getCouponId()))
                .collect(Collectors.toList());
        assertThat(couponDtos)
                .extracting(CouponDto::getId, CouponDto::getName, CouponDto::getDiscountRate, CouponDto::getDiscountPrice)
                .containsExactly(tuple(1L, "정액 할인 쿠폰", 0d, 5000), tuple(2L, "할인율 쿠폰", 10d, 0));
    }

    @Test
    @DisplayName("Member 의 정보를 주면, Member 가 가진 Coupon 을 반환받는다.")
    void findMemberCouponByMember() {
        initMemberCoupon();

        List<MemberCoupon> memberCoupons = couponRepository.findMemberCouponByMember(MEMBER_2);

        assertThat(memberCoupons)
                .hasSize(1)
                .extracting(MemberCoupon::getCoupon)
                .extracting(Coupon::getId, Coupon::getName, Coupon::getDiscountRate, Coupon::getDiscountPrice)
                .containsExactly(tuple(1L, "정액 할인 쿠폰", 0d, 5000));
    }

    private void initMemberCoupon() {
        String memberCouponSaveQuery = "INSERT INTO member_coupon (id, member_id, coupon_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(memberCouponSaveQuery, 1L, 1L, 1L);
        jdbcTemplate.update(memberCouponSaveQuery, 2L, 1L, 2L);
        jdbcTemplate.update(memberCouponSaveQuery, 3L, 2L, 1L);
    }

    @Test
    @DisplayName("Member Coupon Id 로 해당하는 Coupon 을 가져온다.")
    void findByCouponByMemberCouponId() {
        initMemberCoupon();

        Coupon coupon = couponRepository.findCouponByMemberCouponId(3L);

        assertThat(coupon)
                .extracting(Coupon::getId, Coupon::getName, Coupon::getDiscountRate, Coupon::getDiscountPrice)
                .containsExactly(1L, "정액 할인 쿠폰", 0d, 5000);
    }

}