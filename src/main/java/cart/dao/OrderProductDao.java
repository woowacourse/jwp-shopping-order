package cart.dao;

import cart.dao.dto.OrderProductDto;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductDao {

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


}
