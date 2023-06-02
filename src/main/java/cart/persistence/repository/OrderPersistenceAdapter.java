package cart.persistence.repository;

import cart.application.domain.Member;
import cart.application.domain.Order;
import cart.application.domain.OrderInfo;
import cart.application.domain.Product;
import cart.application.repository.OrderRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
        insertIntoOrder(order);
        insertIntoOrderInfo(order.getOrderInfo());
        return order;
    }

    private void insertIntoOrder(Order order) {
        String sql = "INSERT (member_id, original_price, used_point, point_to_add) INTO order " +
                "VALUES (:member_id, :original_price, :used_point, :point_to_add)";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("member_id", order.getMember().getId())
                .addValue("original_price", order.getOriginalPrice())
                .addValue("used_point", order.getUsedPoint())
                .addValue("point_to_add", order.getPointToAdd());
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    private void insertIntoOrderInfo(List<OrderInfo> orderInfo) {
        String sql = "INSERT (order_id, product_id, name, price, image_url, quantity) INTO order_info " +
                "VALUES :order_id and :product_id and :name and :price and :image_url and :quantity";
        SqlParameterSource[] namedParameters = new SqlParameterSource[orderInfo.size()];

        for (OrderInfo info : orderInfo) {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("order_id", info.getId());
            parameters.addValue("product_id", info.getProduct().getId());
            parameters.addValue("name", info.getName());
            parameters.addValue("price", info.getPrice());
            parameters.addValue("image_url", info.getImageUrl());
            parameters.addValue("quantity", info.getQuantity());
        }
        namedParameterJdbcTemplate.batchUpdate(sql, namedParameters);
    }

    @Override
    public List<Order> findByMemberId(Long id) {
        String sql = "SELECT (member.id, member.email, member.password, product.id, product.name, product.price," +
                "product.image_url, product.point_ratio, product.point_available, order_info.id, order_info.name, " +
                "order_info.price, order_info.image_url, order.order_id, order_info.quantity, order.original_price, " +
                "order.used_point, order.point_to_add) " +
                "FROM order " +
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
        String sql = "SELECT (member.id, member.email, member.password, product.id, product.name, product.price," +
                "product.image_url, product.point_ratio, product.point_available, order_info.id, order_info.name, " +
                "order_info.price, order_info.image_url, order.order_id, order_info.quantity, order.original_price, " +
                "order.used_point, order.point_to_add) " +
                "FROM order " +
                "INNER JOIN member ON order.member_id = member.id " +
                "INNER JOIN order_info ON order_info.order_id = order.id " +
                "WHERE order.id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        List<Order> orders = new ArrayList<>();

        namedParameterJdbcTemplate.query(sql, namedParameters, rs -> {
            long orderId = rs.getLong("order.order_id");
            Member member = makeMember(rs);
            List<OrderInfo> orderInfo = new ArrayList<>();
            do {
                orderInfo.add(makeOrderInfo(rs));
            } while (rs.next());

            orders.add(new Order(
                    orderId,
                    member,
                    orderInfo,
                    rs.getInt("order.original_price"),
                    rs.getInt("order.used_point"),
                    rs.getInt("order.point_to_add")
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
                rs.getString("order_info_image_url"),
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
