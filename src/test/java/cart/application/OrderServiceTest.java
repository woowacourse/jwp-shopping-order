package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import cart.dao.MemberDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderDetailResponse;
import cart.entity.MemberEntity;
import cart.exception.IllegalMemberException;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import cart.ui.pageable.Page;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MemberDao memberDao;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    private Member member;
    private List<OrderItem> orderItems;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "치킨", 30000, "url");
        orderItems = List.of(new OrderItem(1L, 3, product));
        member = new Member(1L, "id", "password", 1000);
    }

    @Test
    @DisplayName("주문 정보를 저장한다.")
    void saveOrder() {
        //given
        final CartItem cartItem = new CartItem(1L, 3, new Product(1L, "치킨", 30000, "example.com/chicken"), member);
        given(cartItemRepository.findMembersItemByCartIds(eq(member), eq(List.of(1L))))
                .willReturn(List.of(cartItem));
        willDoNothing().given(cartItemRepository).deleteById(eq(1L));
        willDoNothing().given(memberDao).update(eq(MemberEntity.from(member)));
        given(orderRepository.save(any(Order.class))).willReturn(1L);

        //when
        final Long id = orderService.saveOrder(member, new OrderCreateRequest(
                List.of(1L),
                "7777-7777-7777-7777",
                343,
                300
        ));

        //then
        assertAll(
                () -> assertThat(id).isEqualTo(1L),
                () -> assertThat(member.getPoint()).isEqualTo(1000 - 300 + 9000)
        );
    }

    @Nested
    @DisplayName("주문 아이디로 주문 상세 정보 조회 시 ")
    class FindOrderDetailById {

        @Test
        @DisplayName("올바른 정보가 전달되면 주문 정보를 정상적으로 조회한다.")
        void findOrderDetailById() {
            //given
            final Order order = new Order(
                    1L,
                    member,
                    orderItems,
                    300,
                    3000,
                    LocalDateTime.of(2023, 06, 06, 04, 05, 00)
            );
            given(orderRepository.findById(1L)).willReturn(order);

            //when
            final OrderDetailResponse response = orderService.findOrderDetailById(member, 1L);

            //then
            assertAll(
                    () -> assertThat(response).usingRecursiveComparison()
                            .ignoringFields("products")
                            .isEqualTo(order),
                    () -> assertThat(response.getProducts()).usingRecursiveComparison()
                            .ignoringFields("product")
                            .isEqualTo(orderItems),
                    () -> assertThat(response.getProducts().get(0).getProduct())
                            .usingRecursiveComparison()
                            .isEqualTo(product)
            );
        }

        @Test
        @DisplayName("주문 정보의 소유자와 인증된 사용자 정보가 다를 경우 예외를 던진다.")
        void findOrderItemsByIdWithInvalidMember() {
            //given
            final Member invalidMember = new Member(2L, "invalid", "member", 0);
            final Order order = new Order(
                    1L,
                    invalidMember,
                    orderItems,
                    300,
                    3000,
                    LocalDateTime.of(2023, 06, 06, 04, 05, 00)
            );
            given(orderRepository.findById(1L)).willReturn(order);

            //when
            //then
            assertThatThrownBy(() -> orderService.findOrderDetailById(member, 1L))
                    .isInstanceOf(IllegalMemberException.class);
        }
    }

    @Test
    @DisplayName("사용자의 전체 주문 정보를 조회한다.")
    void findOrdersByMember() {
        //given
        final Order order = new Order(
                1L,
                member,
                orderItems,
                300,
                3000,
                LocalDateTime.of(2023, 06, 06, 04, 05, 00)
        );
        given(orderRepository.findByMember(member, Page.DEFAULT)).willReturn(List.of(order));

        //when
        final List<OrderDetailResponse> responses = orderService.findOrdersByMember(member, Page.DEFAULT);

        //then
        assertAll(
                () -> assertThat(responses).usingRecursiveComparison()
                        .ignoringFields("products")
                        .isEqualTo(List.of(order)),
                () -> assertThat(responses.get(0).getProducts()).usingRecursiveComparison()
                        .ignoringFields("product")
                        .isEqualTo(orderItems),
                () -> assertThat(responses.get(0).getProducts().get(0).getProduct())
                        .usingRecursiveComparison()
                        .isEqualTo(product)
        );
    }
}
