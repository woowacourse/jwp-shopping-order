package cart.application.service.order;

import cart.application.repository.CouponRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.order.OrderRepository;
import cart.application.repository.order.OrderedItemRepository;
import cart.application.service.order.dto.OrderResultDto;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.fixture.ProductFixture;
import cart.ui.MemberAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cart.fixture.MemberFixture.디노;
import static cart.fixture.MemberFixture.레오;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class OrderReadServiceTest {

    @Autowired
    OrderReadService orderReadService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderedItemRepository orderedItemRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    MemberRepository memberRepository;

    private Member leo;
    private long leoId;

    @BeforeEach
    void setUp() {
        leoId = memberRepository.createMember(레오);
        Long dinoId = memberRepository.createMember(디노);

        leo = new Member(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
    }

    @Test
    @DisplayName("사용자의 주문 목록을 조회한다.")
    void findOrdersByMember() {
        Order order1 = new Order(10000, 10000, 0, leo);
        Long order1Id = orderRepository.createOrder(order1);
        // 3000 * 2
        OrderItem orderItem1 = OrderItem.of(order1Id, 2, ProductFixture.꼬리요리);
        // 1000
        OrderItem orderItem2 = OrderItem.of(order1Id, 1, ProductFixture.통구이);
        orderedItemRepository.createOrderItems(List.of(orderItem1, orderItem2));

        Order order2 = new Order(11000, 10000, 1000, leo);
        Long order2Id = orderRepository.createOrder(order2);
        // 3000 * 2
        OrderItem order2OrderItem1 = OrderItem.of(order2Id, 2, ProductFixture.꼬리요리);
        orderedItemRepository.createOrderItems(List.of(order2OrderItem1));

        MemberAuth leo = new MemberAuth(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
        List<OrderResultDto> ordersByMember = orderReadService.findOrdersByMember(leo);

        assertAll(
                () -> assertThat(ordersByMember).hasSize(2),
                () -> assertThat(ordersByMember.get(0).getOrderedItems()).hasSize(2)
        );
    }

    @Test
    @DisplayName("사용자의 특정 주문을 조회한다.")
    void findOneOrderByOrderId() {
        Order order1 = new Order(10000, 10000, 0, leo);
        Long order1Id = orderRepository.createOrder(order1);
        // 3000 * 2
        OrderItem orderItem1 = OrderItem.of(order1Id, 2, ProductFixture.꼬리요리);
        // 1000
        OrderItem orderItem2 = OrderItem.of(order1Id, 1, ProductFixture.통구이);
        orderedItemRepository.createOrderItems(List.of(orderItem1, orderItem2));

        MemberAuth leo = new MemberAuth(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
        OrderResultDto orderResultDto = orderReadService.findOrderBy(order1Id);

        assertAll(
                () -> assertThat(orderResultDto.getOrderedItems()).hasSize(2),
                () -> assertThat(orderResultDto.getUsedPoint()).isEqualTo(0),
                () -> assertThat(orderResultDto.getPaymentPrice()).isEqualTo(10000)
        );
    }


}
