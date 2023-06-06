package cart.dao;

import cart.dao.dto.OrderItemWithProductDto;
import cart.dao.dto.OrderProductDto;
import cart.entity.OrderItemEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Repository
public class OrderItemDao {
    
    private static final int AFFECTED_ROW_COUNT_WHEN_INSERT = 1;
    private static final RowMapper<OrderProductDto> orderProductRowMapper = (rs, rn) -> new OrderProductDto(
            rs.getLong("order_id"),
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url")
    );
    private static final RowMapper<OrderItemWithProductDto> orderItemWithProductRowMapper = (rs, rn) ->
            new OrderItemWithProductDto(
                    rs.getLong("order_item.id"),
                    rs.getLong("order_item.order_id"),
                    rs.getLong("product.id"),
                    rs.getString("product.name"),
                    rs.getInt("product.price"),
                    rs.getString("product.image_url"),
                    rs.getInt("order_item.quantity")
            );

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderItemDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public void insertAll(final List<OrderItemEntity> sources) {
        final SqlParameterSource[] allParams = sources.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        try {
            int[] affectedRows = insertAction.executeBatch(allParams);
            validateAffectedRowsWhenInsertAll(affectedRows);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("잘못된 order id 혹은 product id 입니다.");
        }
    }

    private void validateAffectedRowsWhenInsertAll(final int[] affectedRows) {
        final boolean isNormal = Arrays.stream(affectedRows)
                .allMatch(value -> value == AFFECTED_ROW_COUNT_WHEN_INSERT);
        if (isNormal) {
            return;
        }
        throw new RuntimeException("주문 상품 목록 등록 시 다른 데이터에도 영향이 갔습니다.");
    }

    public List<OrderProductDto> findProductByOrderIds(final List<Long> orderIds) {
        final String sql = "SELECT order_item.order_id, product.id, product.name, product.price, product.image_url " +
                "FROM order_item " +
                "INNER JOIN product ON product.id = order_item.product_id " +
                "WHERE order_item.order_id IN (:ids)";
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", orderIds);
        return namedParameterJdbcTemplate.query(sql, params, orderProductRowMapper);
    }

    public List<OrderItemWithProductDto> findProductDetailByOrderId(final long orderId) {
        final String sql = "SELECT order_item.id, order_item.order_id, " +
                "product.id, product.name, product.price, product.image_url, order_item.quantity " +
                "FROM order_item " +
                "INNER JOIN product ON product.id = order_item.product_id " +
                "WHERE order_item.order_id = ?";
        return jdbcTemplate.query(sql, orderItemWithProductRowMapper, orderId);
    }
}
