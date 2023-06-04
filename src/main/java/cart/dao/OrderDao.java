package cart.dao;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.Products;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<OrderProductJoinDto> orderProductJoinDtoRowMapper = (rs, rowNum) -> {
        return new OrderProductJoinDto(
            rs.getLong("id"),
            rs.getInt("discounted_amount"),
            rs.getInt("delivery_amount"),
            rs.getInt("total_amount"),
            rs.getLong("member_id"),
            rs.getString("address"),
            rs.getLong("product_id"),
            rs.getString("product_name"),
            rs.getInt("product_price"),
            rs.getString("product_image_url")
        );
    };

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("`order`")
            .usingColumns("member_id", "discounted_amount", "address", "delivery_amount", "total_amount")
            .usingGeneratedKeyColumns("id");
    }

    public Order save(final Order order, final Long memberId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", memberId);
        params.put("discounted_amount", order.getDiscountedAmount().getValue());
        params.put("address", order.getAddress());
        params.put("delivery_amount", order.getDeliveryAmount().getValue());
        params.put("total_amount", order.getTotalAmount().getValue());
        final long orderId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        final String productOrderSql = "INSERT INTO product_order(product_id, order_id) "
            + "VALUES (?, ?)";
        final Products products = order.getProducts();
        for (final Product product : products.getValue()) {
            jdbcTemplate.update(productOrderSql, product.getId(), orderId);
        }
        return new Order(orderId, order.getProducts(), order.getTotalAmount(), order.getDiscountedAmount(),
            order.getDeliveryAmount(), order.getAddress());
    }

    public Optional<Order> findById(final Long id) {
        final String sql =
            "SELECT o.id                as id, "
                + "       o.discounted_amount as discounted_amount, "
                + "       o.delivery_amount   as delivery_amount, "
                + "       o.address           as address, "
                + "       o.total_amount      as total_amount, "
                + "       o.member_id         as member_id,"
                + "       p.id                as product_id, "
                + "       p.name              as product_name, "
                + "       p.price             as product_price, "
                + "       p.image_url         as product_image_url "
                + "FROM `order` as o "
                + "         join product_order po on o.id = po.order_id "
                + "         join product p on po.product_id = p.id "
                + "WHERE o.id = ?";
        try {
            final List<OrderProductJoinDto> orderProductJoinDtos = jdbcTemplate.query(sql, orderProductJoinDtoRowMapper,
                id);
            return OrderConverter.convertToOrder(orderProductJoinDtos);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Order> findByMember(final Member member) {
        final String sql =
            "SELECT o.id                as id, "
                + "       o.discounted_amount as discounted_amount, "
                + "       o.delivery_amount   as delivery_amount, "
                + "       o.address           as address, "
                + "       o.total_amount      as total_amount, "
                + "       o.member_id         as memer_id, "
                + "       p.id                as product_id, "
                + "       p.name              as product_name, "
                + "       p.price             as product_price, "
                + "       p.image_url         as product_image_url "
                + "FROM `order` as o "
                + "         join product_order po on o.id = po.order_id "
                + "         join product p on po.product_id = p.id "
                + "WHERE o.member_id = ?";
        final List<OrderProductJoinDto> orderProductJoinDtos = jdbcTemplate.query(sql, orderProductJoinDtoRowMapper,
            member.getId());
        return OrderConverter.convertToOrders(orderProductJoinDtos);
    }
}
