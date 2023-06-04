package cart.dao;

import cart.dao.entity.OrderEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) ->
            new OrderEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("shipping_fee"),
                    rs.getLong("total_products_price"),
                    rs.getLong("used_point"),
                    rs.getTimestamp("created_at").toString()
            );

    public OrderDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("order")
                .usingGeneratedKeyColumns("id");
    }

    public Long createOrder(final OrderEntity orderEntity) {
        String sql = "INSERT INTO `order` (member_id, shipping_fee, total_products_price, used_point) VALUES (?, ?, ?, ?)";
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
        assert keys != null;
        Object id = keys.get("id");
        return ((Number) id).longValue();
    }


    public List<OrderEntity> findByMemberId(Long memberId) {
        String sql = "select * from `order` where member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Optional<OrderEntity> findById(final Long id) {
        final String sql = "SELECT * FROM `order` WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
