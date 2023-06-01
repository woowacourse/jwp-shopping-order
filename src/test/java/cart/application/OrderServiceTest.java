package cart.application;

import cart.dao.cartitem.CartItemDao;
import cart.dao.member.MemberDao;
import cart.dao.order.OrderDao;
import cart.dao.order.OrderItemDao;
import cart.dao.point.PointDao;
import cart.dao.product.ProductDao;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.point.Point;
import cart.domain.product.Product;
import cart.dto.order.OrderItemResponse;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.exception.customexception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cart.fixture.MemberFixture.하디;
import static cart.fixture.MemberFixture.현구막;
import static cart.fixture.ProductFixture.샐러드;
import static cart.fixture.ProductFixture.피자;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class OrderServiceTest {

    private Member 하디_멤버;
    private Member 현구막_멤버;
    private Product 피자_상품;
    private Product 샐러드_상품;
    private CartItem 하디_피자_장바구니_아이템;
    private CartItem 하디_샐러드_장바구니_아이템;
    private CartItem 현구막_피자_장바구니_아이템;
    private Timestamp 지금 = new Timestamp(System.currentTimeMillis());
    private Timestamp 나중 = new Timestamp(지금.getTime() + 1000000);
    private Point 하디_포인트;
    private Point 현구막_포인트;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PointDao pointDao;

    @Autowired
    private CartItemDao cartItemDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
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

        pointDao.createPoint(new Point(1000L, 1000L, 하디_멤버, 나중, 지금));
        pointDao.createPoint(new Point(100000L, 100000L, 현구막_멤버, 나중, 지금));

        하디_포인트 = pointDao.findAllAvailablePointsByMemberId(하디_멤버.getId(), 지금).get(0);
        현구막_포인트 = pointDao.findAllAvailablePointsByMemberId(현구막_멤버.getId(), 지금).get(0);
    }

    @Transactional
    @Test
    void 사용포인트가_전체가격보다_많으면_예외를_던진다() {
        // given
        Long totalPrice = 현구막_피자_장바구니_아이템.getQuantity() * 현구막_피자_장바구니_아이템.getProduct().getPrice();
        OrderRequest orderRequest = new OrderRequest(List.of(현구막_피자_장바구니_아이템.getId()), totalPrice + 1000L, totalPrice);

        // when, then
        assertThatThrownBy(() -> orderService.orderCartItems(현구막_멤버, orderRequest))
                .isInstanceOf(PointExceedTotalPriceException.class);
    }

    @Transactional
    @Test
    void 사용포인트가_보유포인트의_보다_많으면_예외를_던진다() {
        // given
        Long totalPrice = 하디_피자_장바구니_아이템.getQuantity() * 하디_피자_장바구니_아이템.getProduct().getPrice() + 하디_샐러드_장바구니_아이템.getQuantity() * 하디_샐러드_장바구니_아이템.getProduct().getPrice();
        OrderRequest orderRequest = new OrderRequest(List.of(하디_피자_장바구니_아이템.getId(), 하디_샐러드_장바구니_아이템.getId()), 5000L, totalPrice);

        // when, then
        assertThatThrownBy(() -> orderService.orderCartItems(하디_멤버, orderRequest))
                .isInstanceOf(PointNotEnoughException.class);
    }

    @Transactional
    @Test
    void 주문요청에_포함된_장바구니_아이디가_주문한_멤버의_소유가_아니면_없는_장바구니라_처리해_예외를_던진다() {
        // given
        Long totalPrice = 하디_피자_장바구니_아이템.getQuantity() * 하디_피자_장바구니_아이템.getProduct().getPrice() + 하디_샐러드_장바구니_아이템.getQuantity() * 하디_샐러드_장바구니_아이템.getProduct().getPrice();
        OrderRequest orderRequest = new OrderRequest(List.of(하디_피자_장바구니_아이템.getId(), 하디_샐러드_장바구니_아이템.getId()), 1000L, totalPrice);

        // when, then
        assertThatThrownBy(() -> orderService.orderCartItems(현구막_멤버, orderRequest))
                .isInstanceOf(CartItemNotFoundException.class);
    }

    @Transactional
    @Test
    void 주문요청에_포함된_장바구니_아이디를_찾지못하면_예외를_던진다() {
        // given
        Long totalPrice = 하디_피자_장바구니_아이템.getQuantity() * 하디_피자_장바구니_아이템.getProduct().getPrice() + 하디_샐러드_장바구니_아이템.getQuantity() * 하디_샐러드_장바구니_아이템.getProduct().getPrice();
        OrderRequest orderRequest = new OrderRequest(List.of(8000L, 하디_샐러드_장바구니_아이템.getId()), 1000L, totalPrice);

        // when, then
        assertThatThrownBy(() -> orderService.orderCartItems(하디_멤버, orderRequest))
                .isInstanceOf(CartItemNotFoundException.class);
    }

    @Transactional
    @Test
    void 주문요청에_포함된_아이템의_수량이_해당_상품의_재고보다_많으면_예외를_던진다() {
        // given
        productDao.updateStock(피자_상품.getId(), 0L);
        Long totalPrice = 하디_피자_장바구니_아이템.getQuantity() * 하디_피자_장바구니_아이템.getProduct().getPrice() + 하디_샐러드_장바구니_아이템.getQuantity() * 하디_샐러드_장바구니_아이템.getProduct().getPrice();
        OrderRequest orderRequest = new OrderRequest(List.of(하디_피자_장바구니_아이템.getId(), 하디_샐러드_장바구니_아이템.getId()), 1000L, totalPrice);

        // when, then
        assertThatThrownBy(() -> orderService.orderCartItems(하디_멤버, orderRequest))
                .isInstanceOf(CartItemQuantityExcessException.class);
    }

    @Transactional
    @Test
    void 주문요청에_포함된_가격의_총합이_각각의_상품들의_가격의_총합과_일치하지_않으면_예외를_던진다() {
        // given
        Long totalPrice = 하디_피자_장바구니_아이템.getQuantity() * 하디_피자_장바구니_아이템.getProduct().getPrice() + 하디_샐러드_장바구니_아이템.getQuantity() * 하디_샐러드_장바구니_아이템.getProduct().getPrice() + 3000L;
        OrderRequest orderRequest = new OrderRequest(List.of(하디_피자_장바구니_아이템.getId(), 하디_샐러드_장바구니_아이템.getId()), 1000L, totalPrice);

        // when, then
        assertThatThrownBy(() -> orderService.orderCartItems(하디_멤버, orderRequest))
                .isInstanceOf(OrderTotalPriceIsNotMatchedException.class);
    }

    @Transactional
    @Test
    void 주문성공시_포인트가_적립된다() {
        // given
        int prevPointsCount = pointDao.findAllAvailablePointsByMemberId(하디_멤버.getId(), 지금).size();
        Long totalPrice = 하디_피자_장바구니_아이템.getQuantity() * 하디_피자_장바구니_아이템.getProduct().getPrice() + 하디_샐러드_장바구니_아이템.getQuantity() * 하디_샐러드_장바구니_아이템.getProduct().getPrice();
        OrderRequest orderRequest = new OrderRequest(List.of(하디_피자_장바구니_아이템.getId(), 하디_샐러드_장바구니_아이템.getId()), 0L, totalPrice);

        // when
        orderService.orderCartItems(하디_멤버, orderRequest);
        int currentPointsCount = pointDao.findAllAvailablePointsByMemberId(하디_멤버.getId(), 지금).size();

        // then
        assertThat(currentPointsCount).isEqualTo(prevPointsCount + 1);
    }

    @Transactional
    @Test
    void 포인트를_사용한_주문시에_보유_포인트가_차감된다() {
        // given
        long previousSumOfPoints = pointDao.findAllAvailablePointsByMemberId(하디_멤버.getId(), 지금)
                .stream()
                .mapToLong(Point::getLeftPoint)
                .sum();
        Long totalPrice = 하디_피자_장바구니_아이템.getQuantity() * 하디_피자_장바구니_아이템.getProduct().getPrice() + 하디_샐러드_장바구니_아이템.getQuantity() * 하디_샐러드_장바구니_아이템.getProduct().getPrice();
        OrderRequest orderRequest = new OrderRequest(List.of(하디_피자_장바구니_아이템.getId(), 하디_샐러드_장바구니_아이템.getId()), 0L, totalPrice);

        // when
        Long orderId = orderService.orderCartItems(하디_멤버, orderRequest);
        OrderResponse orderResponse = orderService.getOrderById(하디_멤버, orderId);
        Long earnedPoint = orderResponse.getEarnedPoint();
        Long usedPoint = orderResponse.getUsedPoint();

        long currentSumOfPoints = pointDao.findAllAvailablePointsByMemberId(하디_멤버.getId(), 지금)
                .stream()
                .mapToLong(Point::getLeftPoint)
                .sum();

        // then
        assertThat(currentSumOfPoints).isEqualTo(previousSumOfPoints - usedPoint + earnedPoint);
    }

    @Transactional
    @Test
    void 주문성공시_주문한_상품의_재고가_차감된다() {
        // given
        Long previousStock = productDao.findProductById(피자_상품.getId()).get().getStock();
        Long totalPrice = 하디_피자_장바구니_아이템.getQuantity() * 하디_피자_장바구니_아이템.getProduct().getPrice();
        OrderRequest orderRequest = new OrderRequest(List.of(하디_피자_장바구니_아이템.getId()), 0L, totalPrice);

        // when
        orderService.orderCartItems(하디_멤버, orderRequest);
        Long currentStock = productDao.findProductById(피자_상품.getId()).get().getStock();

        // then
        assertThat(currentStock).isEqualTo(previousStock - 하디_피자_장바구니_아이템.getQuantity());
    }

    @Transactional
    @Test
    void 주문성공시_장바구니에서_지운다() {
        // given
        Long totalPrice = 하디_피자_장바구니_아이템.getQuantity() * 하디_피자_장바구니_아이템.getProduct().getPrice();
        OrderRequest orderRequest = new OrderRequest(List.of(하디_피자_장바구니_아이템.getId()), 0L, totalPrice);

        // when
        orderService.orderCartItems(하디_멤버, orderRequest);

        // then
        assertThat(cartItemDao.findCartItemById(하디_피자_장바구니_아이템.getId()))
                .isEmpty();
    }

    @Transactional
    @Test
    void 특정_주문_조회시_조회하는_멤버의_주문이_아니라면_예외를_던진다() {
        // given
        Long totalPrice = 하디_피자_장바구니_아이템.getQuantity() * 하디_피자_장바구니_아이템.getProduct().getPrice() + 하디_샐러드_장바구니_아이템.getQuantity() * 하디_샐러드_장바구니_아이템.getProduct().getPrice();
        OrderRequest orderRequest = new OrderRequest(List.of(하디_피자_장바구니_아이템.getId(), 하디_샐러드_장바구니_아이템.getId()), 1000L, totalPrice);
        Long orderId = orderService.orderCartItems(하디_멤버, orderRequest);

        // when, then
        assertThatThrownBy(() -> orderService.getOrderById(현구막_멤버, orderId))
                .isInstanceOf(IllegalMemberException.class);
    }

    @Transactional
    @Test
    void 특정_주문_조회시_없는_주문아이디면_예외를_던진다() {
        // given
        Long notExistOrderId = 10000000L;

        // when, then
        assertThatThrownBy(() -> orderService.getOrderById(하디_멤버, notExistOrderId))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Transactional
    @Test
    void 특정_주문을_조회할_수_있다() {
        // given
        Long totalPrice = 하디_피자_장바구니_아이템.getQuantity() * 하디_피자_장바구니_아이템.getProduct().getPrice() + 하디_샐러드_장바구니_아이템.getQuantity() * 하디_샐러드_장바구니_아이템.getProduct().getPrice();
        OrderRequest orderRequest = new OrderRequest(List.of(하디_피자_장바구니_아이템.getId(), 하디_샐러드_장바구니_아이템.getId()), 1000L, totalPrice);
        Long orderId = orderService.orderCartItems(하디_멤버, orderRequest);

        // when
        OrderResponse orderResponse = orderService.getOrderById(하디_멤버, orderId);
        List<Long> orderItemIds = orderResponse.getOrderItems()
                .stream()
                .map(OrderItemResponse::getProductId)
                .collect(Collectors.toList());

        // then
        assertThat(orderItemIds).containsAll(List.of(피자_상품.getId(), 샐러드_상품.getId()));
    }

    @Transactional
    @Test
    void 멤버가_주문한_모든_주문을_조회할_수_있다() {
        // given
        Long 피자_주문_가격 = 하디_피자_장바구니_아이템.getQuantity() * 하디_피자_장바구니_아이템.getProduct().getPrice();
        OrderRequest 피자_주문_요청 = new OrderRequest(List.of(하디_피자_장바구니_아이템.getId()), 1000L, 피자_주문_가격);
        Long 샐러드_주문_가격 = 하디_샐러드_장바구니_아이템.getQuantity() * 하디_샐러드_장바구니_아이템.getProduct().getPrice();
        OrderRequest 샐러드_주문_요청 = new OrderRequest(List.of(하디_샐러드_장바구니_아이템.getId()), 1000L, 샐러드_주문_가격);
        orderService.orderCartItems(하디_멤버, 피자_주문_요청);
        orderService.orderCartItems(하디_멤버, 샐러드_주문_요청);

        // when
        List<OrderResponse> orderResponses = orderService.getOrdersByMember(하디_멤버);
        List<Long> orders = new ArrayList<>();
        for (OrderResponse orderResponse : orderResponses) {
            orders.addAll(orderResponse.getOrderItems()
                    .stream()
                    .map(OrderItemResponse::getProductId)
                    .collect(Collectors.toList()));
        }

        // then
        assertThat(orders).containsAll(List.of(피자_상품.getId(), 샐러드_상품.getId()));
    }
}
