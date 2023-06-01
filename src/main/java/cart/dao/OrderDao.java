package cart.dao;

import cart.dao.entity.OrderEntity;
import cart.dao.entity.ProductEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDao {

    private final NamedParameterJdbcOperations jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<OrderResultMap> orderResultMapRowMapper = (rs, rowNum) -> new OrderResultMap(
            rs.getLong("orderId"),
            new ProductEntity(
                    rs.getLong("productId"),
                    rs.getInt("price"),
                    rs.getString("name"),
                    rs.getString("imageUrl")
            ),
            rs.getTimestamp("date").toLocalDateTime(),
            rs.getInt("quantity"),
            rs.getInt("orderPrice")
    );

    public OrderDao(final NamedParameterJdbcOperations jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(OrderEntity orderEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(orderEntity);
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<OrderResultMap> findByMemberId(final Long memberId) {
        final String sql = "SELECT o.id orderId, o.price orderPrice, o.date date, oi.quantity quantity, p.id productId, p.price price, p.name name, p.image_url imageUrl " +
                "FROM order_item oi " +
                "LEFT JOIN orders o ON o.id = oi.order_id " +
                "LEFT JOIN product p ON p.id = oi.product_id " +
                "WHERE o.member_id = :memberId";
        final Map<String, Long> params = Collections.singletonMap("memberId", memberId);
        return jdbcTemplate.query(sql, params, orderResultMapRowMapper);
    }
}
