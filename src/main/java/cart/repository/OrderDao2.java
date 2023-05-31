package cart.repository;

import cart.dao.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Objects;

@Repository
public class OrderDao2 {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao2(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("`order`")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(final OrderEntity orderEntity) {
        final String sql = "INSERT INTO `order` (member_id, coupon_id, shipping_fee, total_price) VALUES(?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );

            ps.setLong(1, orderEntity.getMemberId());
            ps.setLong(2, orderEntity.getCouponId());
            ps.setInt(3, orderEntity.getShippingFee());
            ps.setInt(4, orderEntity.getTotalPrice());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
