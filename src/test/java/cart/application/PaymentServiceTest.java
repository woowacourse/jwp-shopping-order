package cart.application;

import cart.dao.*;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.point.Point;
import cart.domain.point.UsedPoint;
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
import java.util.List;

import static cart.TestFeatures.회원2;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql({"/schema.sql", "/data.sql", "/member2_data.sql"})
class PaymentServiceTest {

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

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate, namedParameterJdbcTemplate);
        purchaseOrderItemDao = new PurchaseOrderItemDao(namedParameterJdbcTemplate, jdbcTemplate, dataSource);
        purchaseOrderDao = new PurchaseOrderDao(namedParameterJdbcTemplate, dataSource, purchaseOrderItemDao);
        memberRewardPointDao = new MemberRewardPointDao(namedParameterJdbcTemplate, jdbcTemplate, dataSource);
        orderMemberUsedPointDao = new OrderMemberUsedPointDao(jdbcTemplate, dataSource, namedParameterJdbcTemplate);
        cartItemDao = new CartItemDao(namedParameterJdbcTemplate, jdbcTemplate);

        paymentService = new PaymentService(productDao, purchaseOrderDao, purchaseOrderItemDao,
                memberRewardPointDao, orderMemberUsedPointDao, cartItemDao);
    }

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
        paymentService.createPurchaseOrder(member, purchaseOrderRequest);

        // then
        PurchaseOrderInfo purchaseOrderInfo = purchaseOrderDao.findById(7L);
        List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderItemDao.findAllByPurchaseOrderId(purchaseOrderInfo.getId());
        Point point = memberRewardPointDao.getPointByOrderId(purchaseOrderInfo.getId());
        List<Point> points = memberRewardPointDao.getAllByMemberId(회원2.getId());
        List<UsedPoint> usedPoints = orderMemberUsedPointDao.getAllUsedPointByOrderId(purchaseOrderInfo.getId());
        List<CartItem> cartItems = cartItemDao.findByMemberId(회원2.getId());

        assertAll(
                () -> assertThat(purchaseOrderInfo.getPayment()).isEqualTo(94_000),
                () -> assertThat(purchaseOrderInfo.getUsedPoint()).isEqualTo(1_000),
                () -> assertThat(purchaseOrderItems.size()).isEqualTo(2),
                () -> assertThat(point).usingRecursiveComparison()
                                       .ignoringFields("createdAt", "expiredAt")
                                       .isEqualTo(new Point(7L, 7_520, null, null)),
                () -> assertThat(points).usingRecursiveFieldByFieldElementComparator()
                                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt", "expiredAt")
                                        .contains(new Point(4L, 0, null, null),
                                                new Point(5L, 200, null, null),
                                                new Point(6L, 1_000, null, null)),
                () -> assertThat(usedPoints).usingRecursiveFieldByFieldElementComparator()
                                            .contains(new UsedPoint(4L, 4L, 500),
                                                    new UsedPoint(5L, 5L, 500)),
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
}
