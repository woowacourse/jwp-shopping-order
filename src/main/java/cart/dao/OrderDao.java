package cart.dao;

import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.OrderWithOrderProductEntities;
import cart.dao.entity.OrderWithOrderProductEntity;
import cart.dao.entity.ProductEntity;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<OrderEntity> orderEntityRowMapper = (rs, rowNum) ->
            new OrderEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getInt("used_point"),
                    rs.getInt("saved_point"),
                    rs.getInt("delivery_fee"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime()
            );

    private final RowMapper<OrderWithOrderProductEntity> withOrderProductEntityRowMapper = (rs, rowNum) -> {
        OrderEntity orderEntity = new OrderEntity(
                rs.getLong("order_id"),
                rs.getLong("member_id"),
                rs.getInt("used_point"),
                rs.getInt("saved_point"),
                rs.getInt("delivery_fee"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                null
        );

        OrderProductEntity orderProductEntity = new OrderProductEntity(
                rs.getLong("order_product_id"),
                rs.getLong("order_id"),
                new ProductEntity(
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("product_price"),
                        rs.getString("product_image_url")
                ),
                rs.getInt("quantity")
        );
        return new OrderWithOrderProductEntity(orderEntity, orderProductEntity);
    };

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id")
                .withTableName("orders")
                .usingColumns("member_id", "used_point", "delivery_fee", "saved_point");
    }

    public Long save(OrderEntity orderEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(orderEntity)).longValue();
    }

    public Optional<OrderEntity> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, orderEntityRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

//    public Optional<OrderWithOrderProductEntities> findById(Long id) {
//        String sql = "SELECT o.id AS order_id, "
//                + "o.member_id AS member_id, "
//                + "o.created_at AS created_at, "
//                + "o.delivery_fee AS delivery_fee, "
//                + "o.used_point AS used_point, "
//                + "o.saved_point AS saved_point, "
//                + "op.id AS order_product_id, "
//                + "op.product_id AS product_id, "
//                + "op.product_name AS product_name, "
//                + "op.product_price AS product_price, "
//                + "op.product_image_url AS product_image_url, "
//                + "op.quantity AS quantity "
//                + "FROM orders o "
//                + "JOIN order_product op ON o.id = op.order_id "
//                + "WHERE o.id = ?";
//
//        try {
//            List<OrderWithOrderProductEntity> orderWithOrderProductEntities =
//                    jdbcTemplate.query(sql, withOrderProductEntityRowMapper, id);
//
//            validateResultIsNotEmpty(orderWithOrderProductEntities);
//            if(orderWithOrderProductEntities.isEmpty()) {
//                return Optional.empty();
//            }
//
//            OrderEntity orderEntity = orderWithOrderProductEntities.get(0).getOrderEntity();
//            List<OrderProductEntity> orderProductEntities = orderWithOrderProductEntities.stream()
//                    .map(OrderWithOrderProductEntity::getOrderProductEntity)
//                    .collect(Collectors.toList());
//
//            return Optional.of(new OrderWithOrderProductEntities(orderEntity, orderProductEntities));
//        } catch (EmptyResultDataAccessException e) {
//            return Optional.empty();
//        }
//    }

    public List<OrderWithOrderProductEntities> findOrdersByMemberId(Long memberId) {
        String sql = "SELECT o.id AS order_id, "
                + "o.member_id AS member_id, "
                + "o.delivery_fee AS delivery_fee, "
                + "o.used_point AS used_point, "
                + "o.saved_point AS saved_point, "
                + "o.created_at AS created_at, "
                + "op.id AS order_product_id, "
                + "op.product_id AS product_id, "
                + "op.product_name AS product_name, "
                + "op.product_price AS product_price, "
                + "op.product_image_url AS product_image_url, "
                + "op.quantity AS quantity "
                + "FROM orders o "
                + "JOIN order_product op ON o.id = op.order_id "
                + "WHERE member_id = ?";

        try {
            List<OrderWithOrderProductEntity> orderWithOrderProductEntities =
                    jdbcTemplate.query(sql, withOrderProductEntityRowMapper, memberId);

            Map<OrderEntity, List<OrderWithOrderProductEntity>> orderEntities = orderWithOrderProductEntities.stream()
                    .collect(Collectors.groupingBy(
                                    OrderWithOrderProductEntity::getOrderEntity,
                                    Collectors.toList()
                            )
                    );

            return orderEntities.entrySet().stream()
                    .map(entry -> {
                        List<OrderProductEntity> productEntities = entry.getValue().stream()
                                .map(OrderWithOrderProductEntity::getOrderProductEntity)
                                .collect(Collectors.toList());

                        return new OrderWithOrderProductEntities(entry.getKey(), productEntities);
                    })
                    .collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }
}
