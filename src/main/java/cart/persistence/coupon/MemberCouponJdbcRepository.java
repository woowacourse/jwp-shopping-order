package cart.persistence.coupon;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.MemberCouponRepository;
import cart.domain.coupon.type.CouponInfo;

@Repository
public class MemberCouponJdbcRepository implements MemberCouponRepository {

	public static final RowMapper<MemberJoinCouponInfo> MEMBER_JOIN_COUPON_INFO_ROW_MAPPER = new MemberJoinCouponInfoRowMapper();

	private final JdbcTemplate jdbcTemplate;

	public MemberCouponJdbcRepository(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Long addCoupon(final Long memberId, final Long serialNumberId) {
		final String sql = "INSERT INTO member_coupon (member_id, coupon_serial_number_id) VALUES (?, ?)";

		final KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] {"ID"});
			ps.setLong(1, memberId);
			ps.setLong(2, serialNumberId);
			return ps;
		}, keyHolder);

		return Objects.requireNonNull(keyHolder.getKey()).longValue();
	}

	@Override
	public MemberCoupon findByMemberId(final Long memberId) {
		final String sql = createBaseCartQuery("WHERE member_coupon.member_id = ?");
		final List<MemberJoinCouponInfo> memberJoinCouponInfos
			= jdbcTemplate.query(sql, MEMBER_JOIN_COUPON_INFO_ROW_MAPPER, memberId);

		return new MemberCoupon(memberId, convertToCouponInfos(memberJoinCouponInfos));
	}

	@Override
	public void deleteByMemberIdAndCouponId(final Long memberId, final Long couponInfoId) {
		final String sql =
			"DELETE FROM member_coupon WHERE member_id = ? AND coupon_serial_number_id IN (SELECT id FROM coupon_serial_number WHERE coupon_id = ?)";
		jdbcTemplate.update(sql, memberId, couponInfoId);
	}

	private String createBaseCartQuery(final String condition) {
		final String sql =
			"SELECT member_coupon.member_id, coupon_serial_number.coupon_id, coupon.name, coupon.discount_type, coupon.discount "
				+ "FROM member_coupon "
				+ "INNER JOIN coupon_serial_number ON member_coupon.coupon_serial_number_id = coupon_serial_number.id "
				+ "INNER JOIN coupon ON coupon_serial_number.coupon_id = coupon.id "
				+ "%s";

		return String.format(sql, condition);
	}

	private List<CouponInfo> convertToCouponInfos(final List<MemberJoinCouponInfo> memberJoinCouponInfos) {
		return memberJoinCouponInfos.stream()
			.map(MemberJoinCouponInfo::getCouponInfo)
			.collect(Collectors.toList());
	}
}
