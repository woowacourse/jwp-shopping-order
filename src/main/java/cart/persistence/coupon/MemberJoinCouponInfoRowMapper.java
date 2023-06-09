package cart.persistence.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MemberJoinCouponInfoRowMapper implements RowMapper<MemberJoinCouponInfo> {
	@Override
	public MemberJoinCouponInfo mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		return new MemberJoinCouponInfo(
			rs.getLong("member_coupon.member_id"),
			rs.getLong("coupon_serial_number.coupon_id"),
			rs.getString("coupon.name"),
			rs.getString("coupon.discount_type"),
			rs.getBigDecimal("coupon.discount")
		);
	}
}
