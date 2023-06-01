package cart.dao;

import cart.dao.member.JdbcTemplateMemberDao;
import cart.dao.member.MemberDao;
import cart.dao.order.JdbcTemplateOrderDao;
import cart.dao.order.JdbcTemplateOrderItemDao;
import cart.dao.order.OrderDao;
import cart.dao.order.OrderItemDao;
import cart.dao.point.JdbcTemplatePointDao;
import cart.dao.point.PointDao;
import cart.dao.product.JdbcTemplateProductDao;
import cart.dao.product.ProductDao;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.point.Point;
import cart.domain.product.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.List;

import static cart.fixture.MemberFixture.하디;
import static cart.fixture.MemberFixture.현구막;
import static cart.fixture.ProductFixture.샐러드;
import static cart.fixture.ProductFixture.피자;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class OrderItemDaoTest {

    private Member 하디_멤버;
    private Member 현구막_멤버;
    private Product 피자_상품;
    private Product 샐러드_상품;
    private Timestamp 지금 = new Timestamp(System.currentTimeMillis());
    private Point 하디_포인트;
    private Point 현구막_포인트;
    private Long 하디_1_주문_아이디;
    private Long 하디_2_주문_아이디;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderItemDao orderItemDao;

    @BeforeEach
    void setUp() {
        MemberDao memberDao = new JdbcTemplateMemberDao(jdbcTemplate);
        ProductDao productDao = new JdbcTemplateProductDao(jdbcTemplate);
        OrderDao orderDao = new JdbcTemplateOrderDao(jdbcTemplate);
        PointDao pointDao = new JdbcTemplatePointDao(jdbcTemplate);
        orderItemDao = new JdbcTemplateOrderItemDao(jdbcTemplate);

        memberDao.addMember(하디);
        memberDao.addMember(현구막);
        하디_멤버 = memberDao.findMemberByEmail(하디.getEmail()).get();
        현구막_멤버 = memberDao.findMemberByEmail(현구막.getEmail()).get();
        Long 피자_아이디 = productDao.createProduct(피자);
        Long 샐러드_아이디 = productDao.createProduct(샐러드);
        피자_상품 = new Product(피자_아이디, 피자.getName(), 피자.getPrice(), 피자.getImageUrl(), 피자.getStock());
        샐러드_상품 = new Product(샐러드_아이디, 샐러드.getName(), 샐러드.getPrice(), 샐러드.getImageUrl(), 샐러드.getStock());

        pointDao.createPoint(new Point(1000L, 1000L, 하디_멤버, 지금, 지금));
        pointDao.createPoint(new Point(1000L, 1000L, 현구막_멤버, 지금, 지금));

        하디_포인트 = pointDao.findAllAvailablePointsByMemberId(하디_멤버.getId(), 지금).get(0);
        현구막_포인트 = pointDao.findAllAvailablePointsByMemberId(현구막_멤버.getId(), 지금).get(0);

        Order firstOrder = new Order(하디_멤버, 0L, 하디_포인트.getEarnedPoint(), 지금);
        하디_1_주문_아이디 = orderDao.createOrder(firstOrder, 하디_포인트.getId());
        Order secondOrder = new Order(하디_멤버, 0L, 하디_포인트.getEarnedPoint(), 지금);
        하디_2_주문_아이디 = orderDao.createOrder(secondOrder, 하디_포인트.getId());

    }

    @Test
    void 주문_상품을_추가하고_조회한다() {
        // given
        OrderItem 피자_주문 = new OrderItem(피자_상품.getId(), 피자_상품.getName(), 피자_상품.getPrice(), 피자_상품.getImageUrl(), 2L);

        // when
        orderItemDao.createOrderItem(하디_1_주문_아이디, 피자_주문);
        List<OrderItem> orderItems = orderItemDao.findOrderItemsByOrderId(하디_1_주문_아이디);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(orderItems.size()).isEqualTo(1);
        softAssertions.assertThat(orderItems.get(0).getProductId()).isEqualTo(피자_상품.getId());
        softAssertions.assertThat(orderItems.get(0).getPrice()).isEqualTo(피자_상품.getPrice());
        softAssertions.assertThat(orderItems.get(0).getName()).isEqualTo(피자_상품.getName());
        softAssertions.assertThat(orderItems.get(0).getImageUrl()).isEqualTo(피자_상품.getImageUrl());
        softAssertions.assertThat(orderItems.get(0).getQuantity()).isEqualTo(2L);
        softAssertions.assertAll();
    }

    @Test
    void 주문한_상품의_목록을_조회한다() {
        // given
        OrderItem 피자_주문 = new OrderItem(피자_상품.getId(), 피자_상품.getName(), 피자_상품.getPrice(), 피자_상품.getImageUrl(), 2L);
        OrderItem 샐러드_주문 = new OrderItem(샐러드_상품.getId(), 샐러드_상품.getName(), 샐러드_상품.getPrice(), 샐러드_상품.getImageUrl(), 1L);
        orderItemDao.createOrderItem(하디_1_주문_아이디, 피자_주문);
        orderItemDao.createOrderItem(하디_1_주문_아이디, 샐러드_주문);

        // when
        List<OrderItem> orderItems = orderItemDao.findOrderItemsByOrderId(하디_1_주문_아이디);

        // then
        assertThat(orderItems.size()).isEqualTo(2);
    }
}
