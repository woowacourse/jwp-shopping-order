package cart.dao;

import cart.dao.cartitem.CartItemDao;
import cart.dao.cartitem.JdbcTemplateCartItemDao;
import cart.dao.member.JdbcTemplateMemberDao;
import cart.dao.member.MemberDao;
import cart.dao.order.JdbcTemplateOrderDao;
import cart.dao.order.OrderDao;
import cart.dao.point.JdbcTemplatePointDao;
import cart.dao.point.PointDao;
import cart.dao.product.JdbcTemplateProductDao;
import cart.dao.product.ProductDao;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
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
import java.util.stream.Collectors;

import static cart.fixture.MemberFixture.하디;
import static cart.fixture.MemberFixture.현구막;
import static cart.fixture.ProductFixture.샐러드;
import static cart.fixture.ProductFixture.피자;

@JdbcTest
public class OrderDaoTest {

    private Member 하디_멤버;
    private Member 현구막_멤버;
    private Product 피자_상품;
    private Product 샐러드_상품;
    private CartItem 하디_피자_장바구니_아이템;
    private CartItem 하디_샐러드_장바구니_아이템;
    private CartItem 현구막_피자_장바구니_아이템;
    private Timestamp 지금 = new Timestamp(System.currentTimeMillis());
    private Point 하디_포인트;
    private Point 현구막_포인트;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;
    private ProductDao productDao;
    private CartItemDao cartItemDao;
    private OrderDao orderDao;
    private PointDao pointDao;

    @BeforeEach
    void setUp() {
        memberDao = new JdbcTemplateMemberDao(jdbcTemplate);
        productDao = new JdbcTemplateProductDao(jdbcTemplate);
        cartItemDao = new JdbcTemplateCartItemDao(jdbcTemplate);
        orderDao = new JdbcTemplateOrderDao(jdbcTemplate);
        pointDao = new JdbcTemplatePointDao(jdbcTemplate);

        memberDao.addMember(하디);
        memberDao.addMember(현구막);
        하디_멤버 = memberDao.findMemberByEmail(하디.getEmail()).get();
        현구막_멤버 = memberDao.findMemberByEmail(현구막.getEmail()).get();
        Long 피자_아이디 = productDao.createProduct(피자);
        Long 샐러드_아이디 = productDao.createProduct(샐러드);
        피자_상품 = new Product(피자_아이디, 피자.getName(), 피자.getPrice(), 피자.getImageUrl(), 피자.getStock());
        샐러드_상품 = new Product(샐러드_아이디, 샐러드.getName(), 샐러드.getPrice(), 샐러드.getImageUrl(), 샐러드.getStock());

        cartItemDao.save(new CartItem(3L, 하디_멤버, 피자_상품));
        하디_피자_장바구니_아이템 = cartItemDao.findCartItemByMemberIdAndProductId(하디_멤버.getId(), 피자_상품.getId()).get();
        cartItemDao.save(new CartItem(3L, 현구막_멤버, 피자_상품));
        현구막_피자_장바구니_아이템 = cartItemDao.findCartItemByMemberIdAndProductId(현구막_멤버.getId(), 피자_상품.getId()).get();
        cartItemDao.save(new CartItem(1L, 하디_멤버, 샐러드_상품));
        하디_샐러드_장바구니_아이템 = cartItemDao.findCartItemByMemberIdAndProductId(하디_멤버.getId(), 샐러드_상품.getId()).get();

        pointDao.createPoint(new Point(1000L, 1000L, 하디_멤버, 지금, 지금));
        pointDao.createPoint(new Point(1000L, 1000L, 현구막_멤버, 지금, 지금));

        하디_포인트 = pointDao.findAllAvailablePointsByMemberId(하디_멤버.getId(), 지금).get(0);
        현구막_포인트 = pointDao.findAllAvailablePointsByMemberId(현구막_멤버.getId(), 지금).get(0);
    }

    @Test
    void 아이템을_주문하고_조회한다() {
        // given
        Order order = new Order(하디_멤버, 200L, 하디_포인트.getEarnedPoint(), 지금);

        // when
        Long orderId = orderDao.createOrder(order, 하디_포인트.getId());

        // then
        Order createdOrder = orderDao.findOrderById(orderId).get();
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(createdOrder.getCreatedAt()).isEqualTo(지금);
        softAssertions.assertThat(createdOrder.getUsedPoint()).isEqualTo(order.getUsedPoint());
        softAssertions.assertThat(createdOrder.getEarnedPoint()).isEqualTo(order.getEarnedPoint());
        softAssertions.assertThat(createdOrder.getMember()).isEqualTo(하디_멤버);
        softAssertions.assertAll();
    }

    @Test
    void 회원으로_주문한_아이템을_조회한다() {
        // given
        Order firstOrder = new Order(하디_멤버, 200L, 하디_포인트.getEarnedPoint(), 지금);
        Long firstOrderId = orderDao.createOrder(firstOrder, 하디_포인트.getId());
        Order secondOrder = new Order(하디_멤버, 300L, 하디_포인트.getEarnedPoint(), 지금);
        Long secondOrderId = orderDao.createOrder(secondOrder, 하디_포인트.getId());

        // when
        List<Order> orders = orderDao.findAllOrdersByMemberId(하디_멤버.getId());
        List<Long> ids = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());
        List<Long> usedPoints = orders.stream()
                .map(Order::getUsedPoint)
                .collect(Collectors.toList());

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(orders.size()).isEqualTo(2);
        softAssertions.assertThat(ids).containsAll(List.of(firstOrderId, secondOrderId));
        softAssertions.assertThat(usedPoints).containsAll(List.of(firstOrder.getUsedPoint(), secondOrder.getUsedPoint()));
        softAssertions.assertAll();
    }
}
