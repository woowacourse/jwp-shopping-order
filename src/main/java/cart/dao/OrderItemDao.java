package cart.dao;

import cart.domain.OrderItemEntity;
import cart.exception.OrderItemNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private static final String TABLE = "order_item";
    private static final String ID = "id";
    private static final String ORDER_ID = "order_id";
    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_NAME = "product_name";
    private static final String PRODUCT_PRICE = "product_price";
    private static final String PRODUCT_IMAGE_URL = "product_image_url";
    private static final String QUANTITY = "quantity";
    private static final String ALL_COLUMN = String.join(", ", ID, ORDER_ID, PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_IMAGE_URL, QUANTITY);

    private static final RowMapper<OrderItemEntity> rowMapper = (resultSet, rowNum) ->
            new OrderItemEntity(
                    resultSet.getLong(ID),
                    resultSet.getLong(ORDER_ID),
                    resultSet.getLong(PRODUCT_ID),
                    resultSet.getString(PRODUCT_NAME),
                    resultSet.getInt(PRODUCT_PRICE),
                    resultSet.getString(PRODUCT_IMAGE_URL),
                    resultSet.getInt(QUANTITY)
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    public long insert(final OrderItemEntity orderItemEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(orderItemEntity);

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public OrderItemEntity findById(final Long id) {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + " WHERE id = ?;";

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (final EmptyResultDataAccessException e) {
            throw new OrderItemNotFoundException();
        }
    }

    public OrderItemEntity findByProductId(final Long productId) {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + " WHERE product_id = ?;";

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, productId);
        } catch (final EmptyResultDataAccessException e) {
            throw new OrderItemNotFoundException();
        }
    }

    public OrderItemEntity findByOrderId(final Long orderId) {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + " WHERE order_id = ?;";

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, orderId);
        } catch (final EmptyResultDataAccessException e) {
            throw new OrderItemNotFoundException();
        }
    }
}
