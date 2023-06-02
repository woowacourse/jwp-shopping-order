package cart.persistence.repository;

import cart.application.domain.Member;
import cart.application.domain.Order;
import cart.application.domain.OrderInfo;
import cart.application.domain.Product;
import cart.application.repository.OrderRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderPersistenceAdapter implements OrderRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderPersistenceAdapter(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Order insert(Order order) {
        Long orderId = insertIntoOrder(order);
        insertIntoOrderInfo(order.getOrderInfo(), orderId);
        return order;
    }

    private Long insertIntoOrder(Order order) {
        String sql = "INSERT INTO orders (member_id, original_price, used_point, point_to_add) " +
                "VALUES (:member_id, :original_price, :used_point, :point_to_add)";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("member_id", order.getMember().getId())
                .addValue("original_price", order.getOriginalPrice())
                .addValue("used_point", order.getUsedPoint())
                .addValue("point_to_add", order.getPointToAdd());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, namedParameters, keyHolder);
        return keyHolder.getKeyAs(Long.class);
    }

    private void insertIntoOrderInfo(List<OrderInfo> orderInfo, Long orderId) {
        String sql = "INSERT INTO order_info (order_id, product_id, name, price, image_url, quantity) " +
                "VALUES (:order_id, :product_id, :name, :price, :image_url, :quantity)";
        SqlParameterSource[] namedParameters = new SqlParameterSource[orderInfo.size()];

        for (int i = 0; i < orderInfo.size(); i++) {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("order_id", orderId);
            parameters.addValue("product_id", orderInfo.get(i).getProduct().getId());
            parameters.addValue("name", orderInfo.get(i).getName());
            parameters.addValue("price", orderInfo.get(i).getPrice());
            parameters.addValue("image_url", orderInfo.get(i).getImageUrl());
            parameters.addValue("quantity", orderInfo.get(i).getQuantity());
            namedParameters[i] = parameters;
        }
        namedParameterJdbcTemplate.batchUpdate(sql, namedParameters);
    }

    @Override
    public List<Order> findByMemberId(Long id) {
        String sql = "SELECT (member.id, member.email, member.password, member.point, product.id, product.name, product.price," +
                "product.image_url, product.point_ratio, product.point_available, order_info.id, order_info.name, " +
                "order_info.price, order_info.image_url, orders.order_id, order_info.quantity, orders.original_price, " +
                "orders.used_point, orders.point_to_add) " +
                "FROM orders " +
                "INNER JOIN member ON order.member_id = member.id " +
                "INNER JOIN order_info ON order_info.order_id = order.id " +
                "WHERE member.id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        List<Order> orders = new ArrayList<>();

        namedParameterJdbcTemplate.query(sql, namedParameters, rs -> {
            long orderId = rs.getLong("order.order_id");
            List<OrderInfo> orderInfo = new ArrayList<>();
            do {
                if (orderId != rs.getLong("order_id")) {
                    orders.add(new Order(
                            orderId,
                            makeMember(rs),
                            orderInfo,
                            rs.getInt("order.original_price"),
                            rs.getInt("order.used_point"),
                            rs.getInt("order.point_to_add")
                    ));
                    orderId = rs.getLong("order_id");
                    orderInfo.clear();
                }
                orderInfo.add(makeOrderInfo(rs));
            } while (rs.next());
        });
        return orders;
    }

    @Override
    public Order findById(Long id) {
        String sql = "SELECT *" +
                "FROM orders " +
                "INNER JOIN member ON orders.member_id = member.id " +
                "INNER JOIN order_info ON order_info.order_id = orders.id " +
                "INNER JOIN product ON order_info.product_id = product.id " +
                "WHERE member.id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        List<Order> orders = new ArrayList<>();

        namedParameterJdbcTemplate.query(sql, namedParameters, rs -> {
            long orderId = rs.getLong("orders.id");
            Member member = makeMember(rs);
            List<OrderInfo> orderInfo = new ArrayList<>();
            Integer originalPrice = rs.getInt("orders.original_price");
            Integer usedPoint = rs.getInt("orders.used_point");
            Integer pointToAdd = rs.getInt("orders.point_to_add");
            do {
                orderInfo.add(makeOrderInfo(rs));
            } while (rs.next());

            orders.add(new Order(
                    orderId,
                    member,
                    orderInfo,
                    originalPrice,
                    usedPoint,
                    pointToAdd
            ));
        });
        return orders.stream()
                .findAny()
                .orElseThrow();
    }

    private Member makeMember(ResultSet rs) throws SQLException {
        return new Member(
                rs.getLong("member.id"),
                rs.getString("member.email"),
                rs.getString("member.password"),
                rs.getInt("member.point")
        );
    }

    private OrderInfo makeOrderInfo(ResultSet rs) throws SQLException {
        return new OrderInfo(
                rs.getLong("order_info.id"),
                makeProduct(rs),
                rs.getString("order_info.name"),
                rs.getInt("order_info.price"),
                rs.getString("order_info.image_url"),
                rs.getInt("order_info.quantity")
        );
    }

    private Product makeProduct(ResultSet rs) throws SQLException {
        Long productId = rs.getLong("product.id");
        String name = rs.getString("product.name");
        int price = rs.getInt("product.price");
        String imageUrl = rs.getString("product.image_url");
        Double pointRatio = rs.getDouble("product.point_ratio");
        Boolean pointAvailable = rs.getBoolean("product.point_available");
        return new Product(productId, name, price, imageUrl, pointRatio, pointAvailable);
    }
}
