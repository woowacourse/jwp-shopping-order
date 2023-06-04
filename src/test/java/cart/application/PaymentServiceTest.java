package cart.application;

import cart.dao.*;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.point.Point;
import cart.domain.point.UsedPoint;
import cart.domain.purchaseorder.OrderStatus;
import cart.domain.purchaseorder.PurchaseOrderInfo;
import cart.domain.purchaseorder.PurchaseOrderItem;
import cart.dto.purchaseorder.request.PurchaseOrderItemRequest;
import cart.dto.purchaseorder.request.PurchaseOrderRequest;
import cart.exception.PointException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cart.TestFeatures.회원2;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql({"/schema.sql", "/data.sql", "/member2_data.sql"})
class PaymentServiceTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private ProductDao productDao;
    private PurchaseOrderDao purchaseOrderDao;
    private PurchaseOrderItemDao purchaseOrderItemDao;
    private MemberRewardPointDao memberRewardPointDao;
    private OrderMemberUsedPointDao orderMemberUsedPointDao;
    private CartItemDao cartItemDao;
    private MemberDao memberDao;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate, namedParameterJdbcTemplate);
        purchaseOrderItemDao = new PurchaseOrderItemDao(namedParameterJdbcTemplate, jdbcTemplate, dataSource);
        purchaseOrderDao = new PurchaseOrderDao(namedParameterJdbcTemplate, dataSource);
        memberRewardPointDao = new MemberRewardPointDao(namedParameterJdbcTemplate, jdbcTemplate, dataSource);
        orderMemberUsedPointDao = new OrderMemberUsedPointDao(jdbcTemplate, dataSource, namedParameterJdbcTemplate);
        cartItemDao = new CartItemDao(namedParameterJdbcTemplate, jdbcTemplate);

        paymentService = new PaymentService(productDao, purchaseOrderDao, purchaseOrderItemDao,
                memberRewardPointDao, orderMemberUsedPointDao, cartItemDao);
    }

    // TODO: 2023/06/04 테스트 다시 살펴보기
    @DisplayName("주문을 수행하면, 각 테이블에 알맞은 정보가 들어어가고 수정되어야 한다")
    @Test
    void createPurchaseOrder() {
        // given
        Member member = 회원2;
        PurchaseOrderRequest purchaseOrderRequest = new PurchaseOrderRequest(
                1_000,
                List.of(
                        new PurchaseOrderItemRequest(1L, 3),
                        new PurchaseOrderItemRequest(3L, 5)
                )
        );

        // when
        Long purchaseOrderId = paymentService.createPurchaseOrder(member, purchaseOrderRequest);

        // then
        PurchaseOrderInfo purchaseOrderInfo = purchaseOrderDao.findById(purchaseOrderId)
                                                              .orElseThrow();
        List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderItemDao.findAllByPurchaseOrderId(purchaseOrderInfo.getId());
        Point point = memberRewardPointDao.getPointByOrderId(purchaseOrderInfo.getId()).get();
        List<Point> points = memberRewardPointDao.getAllByMemberId(회원2.getId());
        List<UsedPoint> usedPoints = orderMemberUsedPointDao.getAllUsedPointByOrderId(purchaseOrderInfo.getId());
        List<CartItem> cartItems = cartItemDao.findByMemberId(회원2.getId());

        assertAll(
                () -> assertThat(purchaseOrderInfo.getPayment()).isEqualTo(94_000),
                () -> assertThat(purchaseOrderInfo.getUsedPoint()).isEqualTo(1_000),
                () -> assertThat(purchaseOrderItems.size()).isEqualTo(2),
                () -> assertThat(point).usingRecursiveComparison()
                                       .ignoringFields("createdAt", "expiredAt")
                                       .isEqualTo(new Point(8L, 7_520, null, null)),
                () -> assertThat(points).usingRecursiveFieldByFieldElementComparator()
                                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt", "expiredAt")
                                        .contains(new Point(4L, 0, null, null),
                                                new Point(5L, 0, null, null),
                                                new Point(6L, 0, null, null),
                                                new Point(7L, 200, null, null),
                                                new Point(8L, 7520, null, null)),
                () -> assertThat(usedPoints).usingRecursiveFieldByFieldElementComparator()
                                            .contains(new UsedPoint(7L, 4L, 0),
                                                    new UsedPoint(8L, 5L, 0),
                                                    new UsedPoint(9L, 6L, 0),
                                                    new UsedPoint(10L, 7L, 1000)),
                () -> assertThat(cartItems.size()).isEqualTo(0)
        );
    }

    @DisplayName("주문을 수행할 떄, 존재 포인트보다 더 많이 사용하려 한다면 예외를 발생한다")
    @Test
    void createPurchaseOrderExceptionPoint() {
        // given
        Member member = 회원2;
        PurchaseOrderRequest purchaseOrderRequest = new PurchaseOrderRequest(
                10_000,
                List.of(
                        new PurchaseOrderItemRequest(1L, 3),
                        new PurchaseOrderItemRequest(3L, 5)
                )
        );

        // then
        assertThatThrownBy(() -> paymentService.createPurchaseOrder(member, purchaseOrderRequest))
                .isInstanceOf(PointException.class);
    }

    @DisplayName("주문을 수행할 떄, 존재하지 않는 상품을 구매하려 하면 예외를 발생한다")
    @Test
    void createPurchaseOrderExceptionProduct() {
        // given
        Member member = 회원2;
        PurchaseOrderRequest purchaseOrderRequest = new PurchaseOrderRequest(
                1_000,
                List.of(
                        new PurchaseOrderItemRequest(100L, 3),
                        new PurchaseOrderItemRequest(3L, 5)
                )
        );

        // then
        assertThatThrownBy(() -> paymentService.createPurchaseOrder(member, purchaseOrderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 취소할 수 있다 - 취소가 가능한 경우")
    @Test
    void deleteOrder() {
        // given
        UsedPoint 주문_사용_포인트1 = new UsedPoint(4L, 4L, 500);
        UsedPoint 주문_사용_포인트2 = new UsedPoint(5L, 5L, 400);
        UsedPoint 주문_사용_포인트3 = new UsedPoint(6L, 6L, 1100);

        Point 이전_주문_적립_포인트1 = new Point(4L, 1000, LocalDateTime.parse("2023-05-20 12:12:12", formatter), LocalDateTime.parse("2023-05-25 12:12:12", formatter));
        Point 이전_주문_적립_포인트2 = new Point(5L, 1100, LocalDateTime.parse("2023-05-18 12:12:12", formatter), LocalDateTime.parse("2023-05-29 12:12:12", formatter));
        Point 이전_주문_적립_포인트3 = new Point(6L, 2100, LocalDateTime.parse("2023-05-15 12:12:12", formatter), LocalDateTime.parse("2023-05-30 12:12:12", formatter));

        Point 취소한_주문_적립_포인트 = new Point(7L, 1100, LocalDateTime.parse("2023-05-15 12:12:12", formatter), LocalDateTime.parse("2023-06-15 12:12:12", formatter));

        PurchaseOrderInfo 취소한_주문 = new PurchaseOrderInfo(7L, 회원2, LocalDateTime.parse("2023-05-31 17:11:12", formatter), 21_000, 2_000, OrderStatus.CANCELED);

        Member member = 회원2;
        Long orderId = 7L;

        // when
        paymentService.deleteOrder(member, orderId);

        assertAll(
                () -> assertThat(orderMemberUsedPointDao.getAllUsedPointByOrderId(orderId))
                        .usingRecursiveFieldByFieldElementComparator()
                        .doesNotContain(주문_사용_포인트1, 주문_사용_포인트2, 주문_사용_포인트3),
                () -> assertThat(memberRewardPointDao.getAllByMemberId(member.getId()))
                        .usingRecursiveFieldByFieldElementComparator()
                        .contains(이전_주문_적립_포인트1, 이전_주문_적립_포인트2, 이전_주문_적립_포인트3)
                        .doesNotContain(취소한_주문_적립_포인트),
                () -> assertThat(purchaseOrderDao.findById(orderId)
                                                 .orElseThrow())
                        .usingRecursiveComparison()
                        .isEqualTo(취소한_주문)
        );
    }

    @DisplayName("주문을 취소할 수 있다 - 이미 취소가 된 경우")
    @Test
    void alreadyDeleteOrder() {
        // given
        Member member = 회원2;
        Long orderId = 8L;

        // then
        assertThatThrownBy(() -> paymentService.deleteOrder(member, orderId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 취소할 수 있다 - 해당 주문에 대한 적립 포인트를 사용한 경우")
    @Test
    void deleteUsedPointOrder() {
        // given
        Member member = 회원2;
        Long orderId = 5L;

        // then
        assertThatThrownBy(() -> paymentService.deleteOrder(member, orderId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
