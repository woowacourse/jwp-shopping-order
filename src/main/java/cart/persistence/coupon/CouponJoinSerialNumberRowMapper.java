package cart.persistence.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CouponJoinSerialNumberRowMapper implements RowMapper<CouponJoinSerialNumber> {
	@Override
	public CouponJoinSerialNumber mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		return new CouponJoinSerialNumber(
			rs.getLong("coupon.id"),
			rs.getString("coupon.discount_type"),
			rs.getString("coupon.name"),
			rs.getBigDecimal("coupon.discount"),
			rs.getLong("coupon_serial_number.id"),
			rs.getString("coupon_serial_number.serial_number"),
			rs.getBoolean("coupon_serial_number.is_issued")
		);
	}
}
