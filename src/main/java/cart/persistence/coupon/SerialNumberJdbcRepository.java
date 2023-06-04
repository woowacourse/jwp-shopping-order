package cart.persistence.coupon;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cart.domain.coupon.SerialNumber;
import cart.domain.coupon.SerialNumberRepository;

@Repository
public class SerialNumberJdbcRepository implements SerialNumberRepository {

	private final JdbcTemplate jdbcTemplate;

	public SerialNumberJdbcRepository(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void generateCouponSerialNumber(final Long couponId, final List<SerialNumber> serialNumbers) {
		final String sql = "INSERT INTO coupon_serial_number (coupon_id, serial_number, is_issued) VALUES (?, ?, ?);";

		jdbcTemplate.batchUpdate(sql, serialNumbers, serialNumbers.size(), (ps, serialNumber) -> {
			ps.setLong(1, couponId);
			ps.setString(2, serialNumber.getSerialNumber());
			ps.setBoolean(3, serialNumber.isIssued());
		});
	}

}
