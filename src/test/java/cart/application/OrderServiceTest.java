package cart.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.request.CartItemRequest;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderDetailResponse;
import cart.dto.response.OrderResponse;
import cart.dto.response.OrdersResponse;
import cart.exception.BadRequestException;

class OrderServiceTest {
    @Mock
    private CouponDao couponDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderItemDao orderItemDao;

    @Mock
    private CartItemDao cartItemDao;

    @Mock
    private MemberCouponDao memberCouponDao;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderDao, orderItemDao, cartItemDao, memberCouponDao, couponDao);
    }

    @Test
    @DisplayName("주문 생성 및 데이터 업데이트")
    public void testAddOrderAndUpdateData() {
        Member member = new Member(1L, "a@a.com", "1234", "John");

        List<CartItemRequest> cartItemRequests = Arrays.asList(
            new CartItemRequest(1L, 2, "Product 1", 1000, "http://www.example.com/product1.jpg"),
            new CartItemRequest(2L, 4, "Product 2", 2000, "http://www.example.com/product2.jpg")
        );

        OrderRequest orderRequest = new OrderRequest(cartItemRequests, 1L);

        List<Long> cartItemIds = cartItemRequests.stream()
            .map(CartItemRequest::getCartItemId)
            .collect(Collectors.toList());

        List<CartItem> cartItems = cartItemIds.stream()
            .map(cartItemId -> new CartItem(cartItemId, 1, null, null))
            .collect(Collectors.toList());

        Long memberCouponId = 1L;
        MemberCoupon memberCoupon = new MemberCoupon(memberCouponId, member, null,
            Timestamp.valueOf(LocalDateTime.of(2023, 6, 30, 0, 0)));

        when(cartItemDao.findByIds(cartItemIds)).thenReturn(cartItems);
        when(memberCouponDao.findByIdIfUsable(member.getId(), memberCouponId)).thenReturn(memberCoupon);

        Long orderId = 1L;

        when(orderDao.save(any(Order.class))).thenReturn(orderId);

        orderService.add(member, orderRequest);

        verify(orderDao, times(1)).save(any(Order.class));
        verify(memberCouponDao, times(1)).updateUsabilityById(memberCouponId);
        verify(cartItemDao, times(1)).deleteByIds(cartItemIds);
    }

    @Test
    @DisplayName("주문 생성 - 카트 아이템이 비어있는 경우")
    public void testAddOrder_EmptyCartItems() {
        Member member = new Member(1L, "a@a.com", "1234", "John");

        OrderRequest orderRequest = new OrderRequest();

        assertThrows(BadRequestException.class, () -> orderService.add(member, orderRequest));
    }

    @Test
    @DisplayName("주문 생성 - 장바구니 아이템이 존재하지 않는 경우")
    public void testAddOrder_NonexistentCartItems() {
        Member member = new Member(1L, "a@a.com", "1234", "John");

        List<CartItemRequest> cartItemRequests = Arrays.asList(
            new CartItemRequest(1L, 2, "Product 1", 1000, "http://www.example.com/product1.jpg"),
            new CartItemRequest(2L, 4, "Product 2", 2000, "http://www.example.com/product2.jpg")
        );

        List<Long> cartItemIds = cartItemRequests.stream()
            .map(CartItemRequest::getCartItemId)
            .collect(Collectors.toList());

        when(cartItemDao.findByIds(cartItemIds)).thenReturn(new ArrayList<>());

        OrderRequest orderRequest = new OrderRequest(cartItemRequests, 1L);

        assertThrows(BadRequestException.class, () -> orderService.add(member, orderRequest));

        verify(cartItemDao, times(1)).findByIds(cartItemIds);
    }

    @Test
    @DisplayName("회원의 주문 목록 조회")
    public void testFindOrdersOfMember() {
        Member member = new Member(1L, "a@a.com", "1234", "John");

        List<OrderItem> orderItems = Arrays.asList(
            new OrderItem(1L, null, null, "Product 1", 1000, "http://www.example.com/product1.jpg", 2),
            new OrderItem(2L, null, null, "Product 2", 2000, "http://www.example.com/product2.jpg", 4)
        );

        List<Order> orders = Arrays.asList(
            new Order(1L, member, orderItems, null),
            new Order(2L, member, orderItems, null),
            new Order(3L, member, orderItems, null)
        );

        when(orderDao.findByMemberId(member.getId())).thenReturn(orders);

        OrdersResponse response = orderService.findOrdersOf(member);

        assertNotNull(response);
        assertEquals(orders.size(), response.getOrders().size());

        verify(orderDao, times(1)).findByMemberId(member.getId());
    }

    @Test
    @DisplayName("회원의 주문 상세 조회")
    public void testFindOrderOfMember() {
        Member member = new Member(1L, "a@a.com", "1234", "John");
        Long orderId = 1L;

        Order orderWithoutOrderItems = new Order(orderId, member, null, null);
        List<OrderItem> orderItems = Arrays.asList(
            new OrderItem(1L, null, null, "Product 1", 1000, "http://www.example.com/product1.jpg", 2),
            new OrderItem(2L, null, null, "Product 2", 2000, "http://www.example.com/product2.jpg", 4)
        );

        when(orderDao.findWithoutOrderItems(orderId)).thenReturn(orderWithoutOrderItems);
        when(orderItemDao.findAllByOrderId(orderId)).thenReturn(orderItems);

        OrderDetailResponse response = orderService.findOrderOf(member, orderId);

        assertNotNull(response);
        assertEquals(orderWithoutOrderItems.getId(), response.getId());

        verify(orderDao, times(1)).findWithoutOrderItems(orderId);
        verify(orderItemDao, times(1)).findAllByOrderId(orderId);
    }

    @Test
    @DisplayName("회원의 주문 삭제")
    public void testRemoveOrderOfMember() {
        Member member = new Member(1L, "a@a.com", "1234", "John");
        Long orderId = 1L;

        assertDoesNotThrow(() -> orderService.remove(member, orderId));

        verify(orderItemDao, times(1)).deleteAllOf(orderId);
        verify(orderDao, times(1)).delete(orderId);
    }
}
