package cart.dao;

import cart.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@ContextConfiguration(classes = OrderDao.class)
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private OrderDao orderDao;

    private Long memberId;
    private final String MEMBER_EMAIL = "test@example.com";
    private Long productId1;
    private final String PRODUCT_NAME_1 = "test1";
    private Long productId2;
    private final String PRODUCT_NAME_2 = "test2";
    private Long pointId;
    private final int EARNED_POINT = 1000;


    @DisplayName("order를 제외한 더미 데이터를 추가한다.")
    @BeforeEach
    void setUp() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO member (email, password) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, MEMBER_EMAIL);
            ps.setString(2, "test");

            return ps;
        }, keyHolder);
        memberId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url, stock) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, PRODUCT_NAME_1);
            ps.setInt(2, 1000);
            ps.setString(3, "test");
            ps.setInt(4, 10);

            return ps;
        }, keyHolder);
        productId1 = Objects.requireNonNull(keyHolder.getKey()).longValue();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url, stock) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, PRODUCT_NAME_2);
            ps.setInt(2, 1000);
            ps.setString(3, "test");
            ps.setInt(4, 10);

            return ps;
        }, keyHolder);
        productId2 = Objects.requireNonNull(keyHolder.getKey()).longValue();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO point (earned_point, left_point, member_id, expired_at, created_at) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setInt(1, EARNED_POINT);
            ps.setInt(2, EARNED_POINT);
            ps.setLong(3, memberId);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            return ps;
        }, keyHolder);

        pointId = Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @DisplayName("ordersId로 orderDetail을 조회한다.")
    @Test
    void getOrderDetailsByOrdersId() {
        // given
        Member member = new Member(memberId, MEMBER_EMAIL, null);
        Point point = new Point(pointId, EARNED_POINT, EARNED_POINT, member, null, null);
        int usedPoint = 50;
        LocalDateTime now = LocalDateTime.now();
        Orders orders = new Orders(member, point, EARNED_POINT, usedPoint, now);
        Long generatedId = insertOrders(orders);
        Orders createdOrders = new Orders(generatedId, orders.getMember(), orders.getPoint(), orders.getEarnedPoint(), orders.getUsedPoint(), orders.getCreatedAt());

        Product product = new Product(productId1, PRODUCT_NAME_1, 0, "testImg", 0);
        int quantity = 10;
        OrderDetail orderDetail = new OrderDetail(createdOrders, product, product.getName(), product.getPrice(), product.getImageUrl(), quantity);

        Product product2 = new Product(productId2, PRODUCT_NAME_2, 0, "testImg", 0);
        int quantity2 = 20;
        OrderDetail orderDetail2 = new OrderDetail(createdOrders, product2, product2.getName(), product2.getPrice(), product2.getImageUrl(), quantity2);
        orderDao.createOrderDetail(orderDetail);
        orderDao.createOrderDetail(orderDetail2);

        // when
        List<OrderDetail> orderDetails = orderDao.getOrderDetailsByOrdersId(generatedId);

        // then
        assertEquals(2, orderDetails.size());
    }

    @DisplayName("memberId로 orders들을 가져온다.")
    @Test
    void getOrdersByMemberId() {
        // given
        Member member = new Member(memberId, MEMBER_EMAIL, null);
        Point point = new Point(pointId, EARNED_POINT, EARNED_POINT, member, null, null);
        int usedPoint = 50;
        LocalDateTime now = LocalDateTime.now();
        Orders orders1 = new Orders(member, point, EARNED_POINT, usedPoint, now);
        Orders orders2 = new Orders(member, point, EARNED_POINT, usedPoint, now);
        insertOrders(orders1);
        insertOrders(orders2);

        // when
        List<Orders> orders = orderDao.getOrdersByMemberId(memberId);

        // then
        assertEquals(2, orders.size());
    }

    @DisplayName("ordersId로 orders를 가져온다.")
    @Test
    void getOrdersByOrdersId() {
        // given
        Member member = new Member(memberId, MEMBER_EMAIL, null);
        Point point = new Point(pointId, EARNED_POINT, EARNED_POINT, member, null, null);
        int usedPoint = 50;
        LocalDateTime now = LocalDateTime.now();
        Orders orders = new Orders(member, point, EARNED_POINT, usedPoint, now);

        // when
        Long generatedId = insertOrders(orders);

        // then
        Orders order = orderDao.getOrdersByOrderId(generatedId);
        assertEquals(member, order.getMember());
        assertEquals(point, order.getPoint());
        assertEquals(usedPoint, order.getUsedPoint());
    }

    @DisplayName("orders를 생성한다.")
    @Test
    void createOrders() {
        // given
        Member member = new Member(memberId, MEMBER_EMAIL, null);
        Point point = new Point(pointId, EARNED_POINT, EARNED_POINT, member, null, null);
        int usedPoint = 50;
        LocalDateTime now = LocalDateTime.now();
        Orders orders = new Orders(member, point, EARNED_POINT, usedPoint, now);

        // when
        Long generatedId = orderDao.createOrders(orders);

        // then
        Orders order = orderDao.getOrdersByOrderId(generatedId);
        assertEquals(member, order.getMember());
        assertEquals(point, order.getPoint());
        assertEquals(EARNED_POINT, order.getEarnedPoint());
        assertEquals(usedPoint, order.getUsedPoint());
    }

    @DisplayName("orderDetail을 생성한다.")
    @Test
    void createOrderDetail() {
        // given
        Member member = new Member(memberId, MEMBER_EMAIL, null);
        Point point = new Point(pointId, EARNED_POINT, EARNED_POINT, member, null, null);
        int usedPoint = 50;
        LocalDateTime now = LocalDateTime.now();
        Orders orders = new Orders(member, point, EARNED_POINT, usedPoint, now);
        Long generatedId = insertOrders(orders);
        Orders createdOrders = new Orders(generatedId, orders.getMember(), orders.getPoint(), orders.getEarnedPoint(), orders.getUsedPoint(), orders.getCreatedAt());

        Product product = new Product(productId1, PRODUCT_NAME_1, 0, "testImg", 0);
        int quantity = 10;
        OrderDetail orderDetail = new OrderDetail(createdOrders, product, product.getName(), product.getPrice(), product.getImageUrl(), quantity);

        // when
        orderDao.createOrderDetail(orderDetail);

        // then
        List<OrderDetail> orderDetails = orderDao.getOrderDetailsByOrdersId(generatedId);
        OrderDetail insertedOrderDetail = orderDetails.get(0);
        assertEquals(createdOrders, insertedOrderDetail.getOrders());
        assertEquals(product, insertedOrderDetail.getProduct());
        assertEquals(quantity, insertedOrderDetail.getOrderQuantity());
    }

    @DisplayName("orders를 임의로 생성한다.")
    private Long insertOrders(Orders orders) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO orders (member_id, point_id, earned_point, used_point, created_at) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, orders.getMember().getId());
            ps.setLong(2, orders.getPoint().getId());
            ps.setInt(3, orders.getEarnedPoint());
            ps.setInt(4, orders.getUsedPoint());
            ps.setTimestamp(5, Timestamp.valueOf(orders.getCreatedAt()));

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @DisplayName("orderDetail을 임의로 생성한다.")
    private Long insertOrderDetail(OrderDetail orderDetail) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO order_detail (orders_id, product_id, product_name, product_price, product_image_url, order_quantity) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, orderDetail.getOrders().getId());
            ps.setLong(2, orderDetail.getProduct().getId());
            ps.setString(3, orderDetail.getProductName());
            ps.setInt(4, orderDetail.getProductPrice());
            ps.setString(5, orderDetail.getProductImageUrl());
            ps.setInt(6, orderDetail.getOrderQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
