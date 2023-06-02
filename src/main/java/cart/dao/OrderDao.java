package cart.dao;

import cart.domain.OrderEntity;
import cart.exception.OrderNotFoundException;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private static final String TABLE = "order_history";
    private static final String ID = "id";
    private static final String DATE_TIME = "date_time";
    private static final String MEMBER_ID = "member_id";
    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_NAME = "product_name";
    private static final String PRODUCT_PRICE = "product_price";
    private static final String PRODUCT_IMAGE_URL = "product_image_url";
    private static final String QUANTITY = "quantity";
    private static final String TOTAL_PRODUCT_PRICE = "total_product_price";
    private static final String TOTAL_DELIVERY_FEE = "total_delivery_fee";
    private static final String USE_POINT = "use_point";
    private static final String TOTAL_PRICE = "total_price";
    private static final String ALL_COLUMN = String.join(", ", ID, DATE_TIME, MEMBER_ID, PRODUCT_ID,
            PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_IMAGE_URL, QUANTITY, TOTAL_PRODUCT_PRICE, TOTAL_DELIVERY_FEE,
            USE_POINT, TOTAL_PRICE
    );

    private static final RowMapper<OrderEntity> rowMapper = (resultSet, rowNum) ->
            new OrderEntity(
                    resultSet.getLong(ID),
                    resultSet.getTimestamp(DATE_TIME).toString(),
                    resultSet.getLong(MEMBER_ID),
                    resultSet.getLong(PRODUCT_ID),
                    resultSet.getString(PRODUCT_NAME),
                    resultSet.getInt(PRODUCT_PRICE),
                    resultSet.getString(PRODUCT_IMAGE_URL),
                    resultSet.getInt(QUANTITY),
                    resultSet.getInt(TOTAL_PRODUCT_PRICE),
                    resultSet.getInt(TOTAL_DELIVERY_FEE),
                    resultSet.getInt(USE_POINT),
                    resultSet.getInt(TOTAL_PRICE)
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    public long insert(final OrderEntity orderEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(orderEntity);

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public OrderEntity findById(final Long id) {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + " WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (final EmptyResultDataAccessException e) {
            throw new OrderNotFoundException();
        }
    }

    public List<OrderEntity> findAll() {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + ";";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public void update(final OrderEntity newOrderEntity) {
        String sql = "UPDATE " + TABLE + " SET " +
                "date_time = ?, " +
                "product_name = ?, " +
                "product_price = ?, " +
                "product_image_url = ?, " +
                "quantity = ?, " +
                "total_product_price = ?, " +
                "total_delivery_fee = ?, " +
                "use_point = ?, " +
                "total_price = ? " +
                "WHERE id = ?;";

        jdbcTemplate.update(sql,
                newOrderEntity.getDateTime(),
                newOrderEntity.getProductName(),
                newOrderEntity.getProductPrice(),
                newOrderEntity.getProductImageUrl(),
                newOrderEntity.getQuantity(),
                newOrderEntity.getTotalProductPrice(),
                newOrderEntity.getTotalDeliveryFee(),
                newOrderEntity.getUsePoint(),
                newOrderEntity.getTotalPrice(),
                newOrderEntity.getId()
        );
    }

    public void deleteById(final Long id) {
        String sql = "DELETE FROM " + TABLE + " WHERE id = ?;";

        jdbcTemplate.update(sql, id);
    }
}
