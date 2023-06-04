package cart.persistence.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cart.domain.coupon.type.CouponInfo;

public class CouponInfoRowMapper implements RowMapper<CouponInfo> {
	@Override
	public CouponInfo mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		return CouponInfo.of(
			rs.getLong("id"),
			rs.getString("discount_type"),
			rs.getString("name"),
			rs.getBigDecimal("discount")
		);
	}
}
