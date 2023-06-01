package cart.dao;

import cart.entity.OrderEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) -> {
        return new OrderEntity(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("shipping_fee"),
                rs.getLong("total_products_price"),
                rs.getLong("used_point"),
                rs.getTimestamp("created_at").toString()
        );
    };

    public long save(final OrderEntity orderEntity) {
        String sql = "INSERT INTO orders (member_id, shipping_fee, total_products_price, used_point) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setLong(1, orderEntity.getMemberId());
            pst.setLong(2, orderEntity.getShippingFee());
            pst.setLong(3, orderEntity.getTotalProductsPrice());
            pst.setLong(4, orderEntity.getUsedPoint());
            return pst;
        }, keyHolder);
        Map<String, Object> keys = keyHolder.getKeys();
        Object id = keys.get("id");
        return ((Number) id).longValue();
    }

    public Optional<OrderEntity> findById(final long id) {
        try {
            String sql = "SELECT * FROM orders WHERE id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<OrderEntity> findAllByMemberId(final long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }
}
