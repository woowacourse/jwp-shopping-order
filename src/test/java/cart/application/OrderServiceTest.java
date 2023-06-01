package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.dao.entity.CartItemEntity;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.exception.CartItemException;
import cart.exception.OrderException;
import cart.repository.mapper.MemberMapper;
import cart.repository.mapper.OrderMapper;
import cart.test.ServiceTest;
import cart.ui.controller.dto.request.OrderRequest;
import cart.ui.controller.dto.response.OrderResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

    private MemberEntity memberEntity;
    private ProductEntity productEntity;
    private OrderEntity orderEntity;
    private OrderProductEntity orderProductEntity;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CartItemDao cartItemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderProductDao orderProductDao;

    @BeforeEach
    void setUp() {
        memberEntity = new MemberEntity("a@a.com", "password1", 1000);
        Long memberId = memberDao.addMember(memberEntity);
        memberEntity = memberEntity.assignId(memberId);

        productEntity = new ProductEntity("치킨", 10000, "http://chicken.com");
        Long productId = productDao.createProduct(productEntity);
        productEntity = productEntity.assignId(productId);

        orderEntity = new OrderEntity(memberEntity, 0, 0, 0);
        Long orderId = orderDao.save(orderEntity);
        orderEntity = orderDao.findById(orderId).get();

        orderProductEntity =
                new OrderProductEntity(orderId, productEntity, "치킨", 10000, "http://chicken.com", 5);
        Long orderProductId = orderProductDao.save(orderProductEntity);
        orderProductEntity = orderProductEntity.assignId(orderProductId);
    }

    @Test
    @DisplayName("getOrders 메서드는 주문 정보 목록을 응답한다.")
    void getOrders() {
        OrderEntity newOrderEntity = new OrderEntity(memberEntity, 0, 0, 0);
        Long newOrderId = orderDao.save(newOrderEntity);
        OrderEntity findNewOrderEntity = orderDao.findById(newOrderId).get();

        OrderProductEntity newOrderProductEntity =
                new OrderProductEntity(newOrderId, productEntity, "치킨", 10000, "http://chicken.com", 5);
        Long newOrderProductId = orderProductDao.save(newOrderProductEntity);

        List<OrderResponse> result = orderService.getOrders(MemberMapper.toDomain(memberEntity));

        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0)).usingRecursiveComparison()
                        .isEqualTo(OrderResponse.from(OrderMapper.toDomain(orderEntity, List.of(orderProductEntity)))),
                () -> assertThat(result.get(1)).usingRecursiveComparison()
                        .isEqualTo(
                                OrderResponse.from(OrderMapper.toDomain(
                                        findNewOrderEntity,
                                        List.of(newOrderProductEntity.assignId(newOrderProductId))
                                ))
                        )
        );
    }

    @Nested
    @DisplayName("getOrderDetail 메서드는 ")
    class GetOrderDetail {

        @Test
        @DisplayName("주문에 접근 권한이 없는 멤버라면 예외를 던진다.")
        void invalidMember() {
            Member member = new Member(-1L, "b@b.com", "password2", 0);

            assertThatThrownBy(() -> orderService.getOrderDetail(orderEntity.getId(), member))
                    .isInstanceOf(OrderException.class)
                    .hasMessage("해당 주문을 관리할 수 있는 멤버가 아닙니다.");
        }

        @Test
        @DisplayName("주문과 멤버가 유효하다면 주문 정보를 응답한다.")
        void getOrder() {
            Member member = MemberMapper.toDomain(memberEntity);

            OrderResponse response = orderService.getOrderDetail(orderEntity.getId(), member);

            Order order = OrderMapper.toDomain(orderEntity, List.of(orderProductEntity));
            OrderResponse expected = OrderResponse.from(order);
            assertThat(response).usingRecursiveComparison().isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("processOrder 메서드는 ")
    class ProcessOrder {

        @Test
        @DisplayName("멤버 장바구니 상품이 아니라면 예외를 던진다.")
        void notOwner() {
            Member otherMember = new Member(-1L, "b@b.com", "1234", 0);
            CartItemEntity cartItemEntity = new CartItemEntity(memberEntity, productEntity, 5);
            Long cartItemId = cartItemDao.save(cartItemEntity);
            OrderRequest request = new OrderRequest(List.of(cartItemId), 0);

            assertThatThrownBy(() -> orderService.processOrder(otherMember, request))
                    .isInstanceOf(CartItemException.class)
                    .hasMessage("장바구니 상품을 관리할 수 있는 멤버가 아닙니다.");
        }

        @Test
        @DisplayName("사용하는 포인트가 총 주문 금액보다 크다면 예외를 던진다.")
        void overPointUse() {
            MemberEntity otherMember = new MemberEntity("b@b.com", "1234", 1000);
            Long otherMemberId = memberDao.addMember(otherMember);
            MemberEntity savedMember = otherMember.assignId(otherMemberId);
            CartItemEntity cartItemEntityA = new CartItemEntity(savedMember, productEntity, 5);
            Long cartItemA = cartItemDao.save(cartItemEntityA);
            CartItemEntity cartItemEntityB = new CartItemEntity(savedMember, productEntity, 7);
            Long cartItemB = cartItemDao.save(cartItemEntityB);
            OrderRequest request = new OrderRequest(List.of(cartItemA, cartItemB), 120001);

            assertThatThrownBy(() -> orderService.processOrder(MemberMapper.toDomain(savedMember), request))
                    .isInstanceOf(OrderException.class)
                    .hasMessage("사용하려는 포인트가 총 결제 금액보다 큽니다. 총 결제 금액: " + 120000 + ", 사용 포인트: " + 120001);
        }

        @Test
        @DisplayName("장바구니 목록을 주문하고 멤버 포인트는 감소하고 남은 장바구니 목록을 응답한다.")
        void processOrder() {
            MemberEntity otherMember = new MemberEntity("b@b.com", "1234", 1000);
            Long otherMemberId = memberDao.addMember(otherMember);
            MemberEntity savedMember = otherMember.assignId(otherMemberId);
            CartItemEntity cartItemEntityA = new CartItemEntity(savedMember, productEntity, 5);
            Long cartItemA = cartItemDao.save(cartItemEntityA);
            CartItemEntity cartItemEntityB = new CartItemEntity(savedMember, productEntity, 7);
            Long cartItemB = cartItemDao.save(cartItemEntityB);
            CartItemEntity cartItemEntityC = new CartItemEntity(savedMember, productEntity, 10);
            cartItemDao.save(cartItemEntityC);
            OrderRequest request = new OrderRequest(List.of(cartItemA, cartItemB), 500);

            orderService.processOrder(MemberMapper.toDomain(savedMember), request);

            List<OrderEntity> orders = orderDao.findAllByMemberId(savedMember.getId());
            List<OrderProductEntity> orderProducts = orderProductDao.findAllByOrderId(orders.get(0).getId());
            List<CartItemEntity> cartItems = cartItemDao.findByMemberId(savedMember.getId());
            MemberEntity member = memberDao.getMemberById(savedMember.getId()).get();
            assertAll(
                    () -> assertThat(orders).hasSize(1),
                    () -> assertThat(orderProducts).hasSize(2),
                    () -> assertThat(cartItems).hasSize(1),
                    () -> assertThat(member).usingRecursiveComparison()
                            .ignoringExpectedNullFields()
                            .isEqualTo(new MemberEntity(
                                    savedMember.getId(),
                                    savedMember.getEmail(),
                                    savedMember.getPassword(),
                                    12500)
                            )
            );
        }
    }
}
