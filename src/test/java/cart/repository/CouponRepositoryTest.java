package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.domain.Member;
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

    private static final Member MEMBER = new Member(1L, "a@a", "1234");
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
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.couponRepository = new CouponRepository(
                new CouponDao(dataSource),
                new MemberCouponDao(dataSource),
                new MemberDao(dataSource)
        );

        String couponSaveQuery = "INSERT INTO coupon (id, name, discount_rate, discount_price) VALUES(?, ?, ?, ?)";
        String memberSaveQuery = "INSERT INTO member (id, email, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(couponSaveQuery, 1L, "정액 할인 쿠폰", 0d, 5000);
        jdbcTemplate.update(couponSaveQuery, 2L, "할인율 쿠폰", 10d, 0);
        jdbcTemplate.update(memberSaveQuery, MEMBER.getId(), MEMBER.getEmail(), MEMBER.getPassword());
    }

    @Test
    @DisplayName("사용자가 쿠폰을 추가하면, 현재 Coupon 테이블에 있는 쿠폰이 모두 추가된다.")
    void addCoupon() {
        couponRepository.saveCoupon(MEMBER);

        String findMemberCouponQuery = "SELECT * FROM member_coupon WHERE member_id = ?";
        String findCouponQuery = "SELECT * FROM coupon WHERE id = ?";
        List<MemberCouponDto> memberCouponDtos = jdbcTemplate.query(findMemberCouponQuery, memberCouponDtoRowMapper, MEMBER.getId());

        List<CouponDto> couponDtos = memberCouponDtos.stream()
                .map(memberCouponDto -> jdbcTemplate.queryForObject(findCouponQuery, couponDtoRowMapper,
                        memberCouponDto.getCouponId()))
                .collect(Collectors.toList());
        assertThat(couponDtos)
                .extracting(CouponDto::getId, CouponDto::getName, CouponDto::getDiscountRate, CouponDto::getDiscountPrice)
                .containsExactly(tuple(1L, "정액 할인 쿠폰", 0d, 5000), tuple(2L, "할인율 쿠폰", 10d, 0));

    }

    // TODO: Member 에 맞는 쿠폰 전체 조회 (그냥 MemberId 로 Member_Coupon 을 가져오고, 그 다음에 그것들을 가지고 Coupon 을 가져오면 됨)

    // TODO: Member 와 CouponId 가 주어졌을 때, 해당 Member에 맞는 Coupon를 가져옴, 즉 MemberCoupon 이 아닌 Coupon 을 가져와야함

}