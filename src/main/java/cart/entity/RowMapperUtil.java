package cart.entity;

import org.springframework.jdbc.core.RowMapper;

public class RowMapperUtil {

    public static final RowMapper<OrderEntity> orderEntityRowMapper = (rs, rn) -> new OrderEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getInt("total_price"),
            rs.getInt("discount_price")
    );
}
