package cart.dao;

import cart.dao.dto.OrderProductDto;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductDao {

    private static final RowMapper<OrderProductDto> orderProductDtoRowMapper = (rs, rn) -> new OrderProductDto(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getLong("product_id"),
            rs.getLong("quantity")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderProductDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("orders_product")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(OrderProductDto orderProductDto) {
        Map<String, Object> params = Map.of(
                "order_id", orderProductDto.getOrderId(),
                "product_id", orderProductDto.getProductId(),
                "quantity", orderProductDto.getQuantity()
        );

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }


    public Optional<OrderProductDto> findById(final Long id) {
        String sql = "SELECT * from orders_product where id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, orderProductDtoRowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
