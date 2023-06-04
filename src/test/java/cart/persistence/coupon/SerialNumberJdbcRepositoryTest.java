package cart.persistence.coupon;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import cart.domain.coupon.SerialNumber;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class SerialNumberJdbcRepositoryTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private SerialNumberJdbcRepository serialNumberJdbcRepository;

	private final RowMapper<SerialNumber> cartProductDtoRowMapper = (rs, rowNum) -> new SerialNumber(
		rs.getLong("id"),
		rs.getString("serial_number"),
		rs.getBoolean("is_issued")
	);

	@BeforeEach
	public void setUp() {
		serialNumberJdbcRepository = new SerialNumberJdbcRepository(jdbcTemplate);
	}

	@Test
	void generateCouponSerialNumber() {
		// given
		final long couponId = 4L;
		final List<SerialNumber> serialNumbers = Arrays.asList(
			new SerialNumber(null, "serialNumber1", false),
			new SerialNumber(null, "serialNumber2", false),
			new SerialNumber(null, "serialNumber3", false)
		);

		serialNumberJdbcRepository.generateCouponSerialNumber(couponId, serialNumbers);

		// then
		final String sql = "SELECT * FROM coupon_serial_number WHERE coupon_id = ?";
		final List<SerialNumber> result = jdbcTemplate.query(sql, cartProductDtoRowMapper,
			couponId);

		final List<String> actual = result.stream()
			.map(SerialNumber::getSerialNumber)
			.collect(Collectors.toList());

		final List<String> expected = serialNumbers.stream()
			.map(SerialNumber::getSerialNumber)
			.collect(Collectors.toList());

		assertThat(result).hasSize(serialNumbers.size());
		assertThat(actual.containsAll(expected)).isTrue();

	}
}
