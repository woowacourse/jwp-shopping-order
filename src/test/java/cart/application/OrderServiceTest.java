package cart.application;

import static cart.fixture.DomainFixture.MEMBER_A;
import static cart.fixture.DomainFixture.MEMBER_B;
import static cart.fixture.DomainFixture.PIZZA;
import static cart.fixture.RepositoryFixture.cartItemRepository;
import static cart.fixture.RepositoryFixture.memberRepository;
import static cart.fixture.RepositoryFixture.orderRepository;
import static cart.fixture.ValueFixture.DEFAULT_SIZE;
import static cart.fixture.ValueFixture.LAST_ID_OF_FIRST_PAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.PointDiscountPolicy;
import cart.domain.PointEarnPolicy;
import cart.exception.MemberException;
import cart.exception.OrderException;
import cart.exception.PointException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderServiceTest {

    MemberRepository memberRepository;
    CartItemRepository cartItemRepository;
    OrderRepository orderRepository;
    OrderService orderService;
    CartItem cartItem;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        memberRepository = memberRepository(jdbcTemplate);
        cartItemRepository = cartItemRepository(jdbcTemplate);
        orderRepository = orderRepository(jdbcTemplate);
        orderService = new OrderService(memberRepository, cartItemRepository, orderRepository, 
                PointDiscountPolicy.DEFAULT, PointEarnPolicy.DEFAULT);
        CartItem persistCartItem = new CartItem(MEMBER_A, PIZZA);
        Long persistCartItemId = cartItemRepository.save(MEMBER_A, persistCartItem);
        cartItem = new CartItem(persistCartItemId, persistCartItem.getQuantity(), persistCartItem.getMember(),
                persistCartItem.getProduct());
    }

    @Test
    @DisplayName("order는 주문한 회원, 주문할 장바구니 상품, 사용할 포인트를 전달하면 주문을 저장하고 주문 ID를 반환한다.")
    void orderSuccessTest() {
        List<Long> orderCartItemIds = List.of(cartItem.getId());

        Long actualOrderId = orderService.order(MEMBER_A, orderCartItemIds, 0);

        Member actualMember = memberRepository.findByEmail(MEMBER_A.getEmail()).get();

        assertAll(
                () -> assertThat(actualOrderId).isPositive(),
                () -> assertThat(actualMember.getPoint()).isEqualTo(4_800)
        );
    }

    @Test
    @DisplayName("order는 주문한 회원이 가지고 있는 포인트보다 큰 포인트를 소모하면 예외가 발생한다.")
    void orderFailTestWithUsedPointsGreaterThanCurrentPoints() {
        List<Long> orderCartItemIds = List.of(cartItem.getId());

        assertThatThrownBy(() -> orderService.order(MEMBER_A, orderCartItemIds, 4_000))
                .isInstanceOf(MemberException.TooManyUsedPoints.class)
                .hasMessage("사용할 포인트가 소지 포인트보다 많습니다.");
    }

    @Test
    @DisplayName("order는 포인트 할인 정책의 제한보다 큰 포인트를 소모하면 예외가 발생한다.")
    void orderFailTestWithUsedPointsGreaterThanPointDiscountPolicyCondition() {
        List<Long> orderCartItemIds = List.of(cartItem.getId());

        assertThatThrownBy(() -> orderService.order(MEMBER_A, orderCartItemIds, 3_500))
                .isInstanceOf(PointException.InvalidPolicy.class)
                .hasMessageContaining("사용할 수 있는 포인트 이상을 지정했습니다.");
    }

    @Nested
    @DisplayName("주문 조회 테스트")
    class SelectOrderTest {

        Long firstOrderId;
        Long secondOrderId;
        Long thirdOrderId;

        @BeforeEach
        void setUp() {
            firstOrderId = orderService.order(MEMBER_A, List.of(cartItem.getId()), 0);
            secondOrderId = orderService.order(MEMBER_A, List.of(cartItem.getId()), 0);
            thirdOrderId = orderService.order(MEMBER_A, List.of(cartItem.getId()), 0);
        }

        @Test
        @DisplayName("findByMemberAndOrderId는 회원과 주문 ID를 전달하면 그에 맞는 주문 내역을 반환한다.")
        void findByMemberAndOrderIdSuccessTest() {
            Order actual = orderService.findByMemberAndOrderId(MEMBER_A, firstOrderId);

            assertThat(actual.getId()).isEqualTo(firstOrderId);
        }

        @Test
        @DisplayName("findByMemberAndOrderId는 주문 ID를 주문하지 않은 회원을 전달하면 예외가 발생한다.")
        void findByMemberAndOrderIdFailTest() {
            assertThatThrownBy(() -> orderService.findByMemberAndOrderId(MEMBER_B, firstOrderId))
                    .isInstanceOf(OrderException.IllegalMember.class)
                    .hasMessage("해당 사용자의 주문 내역이 아닙니다.");
        }

        @Test
        @DisplayName("findByMemberAndOrderId는 회원과 마지막 조회 주문 ID, 페이지 크기를 보여주면 최신 순으로 주문 목록을 반환한다.")
        void findByMemberAndLastOrderIdSuccessTest() {
            List<Order> actual = orderService.findByMemberAndLastOrderId(MEMBER_A, LAST_ID_OF_FIRST_PAGE, DEFAULT_SIZE);

            assertAll(
                    () -> assertThat(actual).hasSize(3),
                    () -> assertThat(actual.get(0).getId()).isEqualTo(thirdOrderId),
                    () -> assertThat(actual.get(1).getId()).isEqualTo(secondOrderId),
                    () -> assertThat(actual.get(2).getId()).isEqualTo(firstOrderId)
            );
        }
    }
}
