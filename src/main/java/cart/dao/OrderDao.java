package cart.dao;

import cart.entity.OrderEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("cart_order")
                .usingGeneratedKeyColumns("id");
    }

    private RowMapper<OrderEntity> rowMapper() {
        return (rs, rowNum) -> new OrderEntity(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getInt("delivery_fee")
        );
    }

    public Long create(Long memberId, int deliveryFee) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", memberId)
                .addValue("delivery_fee", deliveryFee);

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<OrderEntity> findAllByMemberId(Long memberId) {
        try {
            String sql = "select * from cart_order where member_id = ?";

            return jdbcTemplate.query(sql, rowMapper(), memberId);
        } catch (final EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public Optional<OrderEntity> findById(Long id) {
        try {
            String sql = "select * from cart_order where id = ?";

            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper(), id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
