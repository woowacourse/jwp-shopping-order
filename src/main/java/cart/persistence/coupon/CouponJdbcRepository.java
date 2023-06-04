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
import org.springframework.transaction.annotation.Transactional;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.coupon.SerialNumber;
import cart.domain.coupon.type.CouponInfo;
import cart.error.exception.BadRequestException;

@Repository
public class CouponJdbcRepository implements CouponRepository {

	private static final RowMapper<CouponInfo> COUPON_INFO_ROW_MAPPER = new CouponInfoRowMapper();
	public static final RowMapper<CouponJoinSerialNumber> COUPON_JOIN_SERIAL_NUMBER_ROW_MAPPER = new CouponJoinSerialNumberRowMapper();

	private final JdbcTemplate jdbcTemplate;

	public CouponJdbcRepository(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Long createCoupon(final CouponInfo couponInfo) {
		final String sql = "INSERT INTO coupon (name, discount_type, discount) VALUES (?, ?, ?)";

		final KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] {"ID"});
			ps.setString(1, couponInfo.getName());
			ps.setString(2, couponInfo.getCouponType().name());
			ps.setBigDecimal(3, couponInfo.getDiscount());
			return ps;
		}, keyHolder);

		return Objects.requireNonNull(keyHolder.getKey()).longValue();
	}

	@Override
	public List<CouponInfo> findAll() {
		final String sql = "SELECT * FROM coupon";
		return jdbcTemplate.query(sql, COUPON_INFO_ROW_MAPPER);
	}

	@Override
	public Coupon findCouponById(final Long couponId) {
		final String sql = createBaseOrderQuery("WHERE coupon.id = ?");
		final List<CouponJoinSerialNumber> couponJoinSerialNumbers = jdbcTemplate.query(sql,
			COUPON_JOIN_SERIAL_NUMBER_ROW_MAPPER, couponId);
		validateCouponExist(couponJoinSerialNumbers);
		return convertToCoupon(couponJoinSerialNumbers);
	}

	@Override
	public void updateCouponInfo(final CouponInfo couponInfo) {
		final String sql = "UPDATE coupon SET name = ?, discount_type = ?, discount = ? WHERE id = ?";
		jdbcTemplate.update(sql, couponInfo.getName(), couponInfo.getCouponType().name(), couponInfo.getDiscount(),
			couponInfo.getId());
	}

	@Transactional
	@Override
	public void removeCoupon(final Long couponId) {
		final String memberCouponDeleteSql = "DELETE FROM member_coupon WHERE coupon_serial_number_id IN (SELECT id FROM coupon_serial_number WHERE coupon_id = ?)";
		jdbcTemplate.update(memberCouponDeleteSql, couponId);

		final String serialNumberDeleteSql = "DELETE FROM coupon_serial_number WHERE coupon_id = ?";
		jdbcTemplate.update(serialNumberDeleteSql, couponId);

		final String couponDeleteSql = "DELETE FROM coupon WHERE id = ?";
		jdbcTemplate.update(couponDeleteSql, couponId);
	}

	private String createBaseOrderQuery(final String condition) {
		final String sql =
			"SELECT coupon.id, coupon.discount_type, coupon.name, coupon.discount, "
				+ "coupon_serial_number.id,coupon_serial_number.serial_number, coupon_serial_number.is_issued "
				+ "FROM coupon "
				+ "JOIN coupon_serial_number ON coupon.id = coupon_serial_number.coupon_id "
				+ "%s";

		return String.format(sql, condition);
	}

	private void validateCouponExist(final List<CouponJoinSerialNumber> couponJoinSerialNumbers) {
		if (couponJoinSerialNumbers.isEmpty()) {
			throw new BadRequestException.Coupon();
		}
	}

	private Coupon convertToCoupon(final List<CouponJoinSerialNumber> couponJoinSerialNumbers) {
		final List<SerialNumber> serialNumbers = convertToSerialNumbers(couponJoinSerialNumbers);
		final CouponInfo couponInfo = couponJoinSerialNumbers.get(0).getCouponInfo();

		return new Coupon(couponInfo, serialNumbers);
	}

	private List<SerialNumber> convertToSerialNumbers(final List<CouponJoinSerialNumber> couponJoinSerialNumbers) {
		return couponJoinSerialNumbers.stream()
			.map(CouponJoinSerialNumber::getSerialNumber)
			.collect(Collectors.toList());
	}
}
