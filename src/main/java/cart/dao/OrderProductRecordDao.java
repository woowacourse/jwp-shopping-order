package cart.dao;

import cart.dao.entity.OrderProductRecordEntity;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductRecordDao {

    private static final RowMapper<OrderProductRecordEntity> ROW_MAPPER = (rs, rowNum) -> new OrderProductRecordEntity(
            rs.getLong("id"),
            rs.getLong("order_product_id"),
            rs.getLong("product_id")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderProductRecordDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_product_record")
                .usingGeneratedKeyColumns("id")
                .usingColumns("order_product_id", "product_id");
    }

    public Optional<OrderProductRecordEntity> findById(Long id) {
        String sql = "SELECT id, order_product_id, product_id FROM order_product_record WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long save(OrderProductRecordEntity orderProductRecordEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(orderProductRecordEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }
}
