package cart.order.application;

import cart.cartitem.dao.CartItemDao;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.application.dto.OrderCartItemDto;
import cart.order.application.dto.OrderDto;
import cart.order.application.dto.OrderItemDto;
import cart.order.application.dto.OrderedProductDto;
import cart.order.dao.OrderHistoryDao;
import cart.order.dao.OrderItemDao;
import cart.order.domain.OrderHistory;
import cart.order.ui.request.OrderCartItemRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static cart.fixtures.CartItemFixtures.Member_Dooly_CartItem1;
import static cart.fixtures.CartItemFixtures.Member_Dooly_CartItem2;
import static cart.fixtures.MemberFixtures.Member_Dooly;
import static cart.fixtures.ProductFixtures.CHICKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private CartItemDao cartItemDao;

    @Mock
    private OrderHistoryDao orderHistoryDao;

    @Mock
    private OrderItemDao orderItemDao;

    @Mock
    private MemberDao memberDao;

    @Test
    void 특정_유저가_주문한_목록을_가져온다() {
        // given
        final OrderHistory orderHistory = new OrderHistory(1L, Member_Dooly.ENTITY, 100000L, LocalDateTime.now());
        final OrderItemDto orderItemDto = new OrderItemDto(1L, orderHistory, CHICKEN.ID, CHICKEN.NAME, CHICKEN.PRICE, CHICKEN.IMAGE_URL, 10);
        final OrderedProductDto orderedProductDto = new OrderedProductDto(CHICKEN.ENTITY, 10);
        when(orderHistoryDao.findByMemberId(Member_Dooly.ID)).thenReturn(List.of(orderHistory));
        when(orderHistoryDao.findById(1L)).thenReturn(orderHistory);
        when(orderItemDao.findByOrderHistoryId(orderHistory.getId())).thenReturn(List.of(orderItemDto));

        // when
        final List<OrderDto> orderDtos = orderService.findAllByMemberId(Member_Dooly.ID);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(orderDtos).hasSize(1);
            softAssertions.assertThat(orderDtos.get(0).getOrderHistory()).usingRecursiveComparison().isEqualTo(orderHistory);
            softAssertions.assertThat(orderDtos.get(0).getProducts()).hasSize(1);
            softAssertions.assertThat(orderDtos.get(0).getProducts().get(0)).usingRecursiveComparison().isEqualTo(orderedProductDto);
        });
    }

    @Test
    void 특정_주문에_대한_주문_상세_정보를_가져온다() {
        // given
        final OrderHistory orderHistory = new OrderHistory(1L, Member_Dooly.ENTITY, 100000L, LocalDateTime.now());
        final OrderItemDto orderItemDto = new OrderItemDto(1L, orderHistory, CHICKEN.ID, CHICKEN.NAME, CHICKEN.PRICE, CHICKEN.IMAGE_URL, 10);
        final OrderedProductDto orderedProductDto = new OrderedProductDto(CHICKEN.ENTITY, 10);
        when(orderHistoryDao.findById(1L)).thenReturn(orderHistory);
        when(orderItemDao.findByOrderHistoryId(orderHistory.getId())).thenReturn(List.of(orderItemDto));

        // when
        final OrderDto orderDto = orderService.findByOrderHistoryId(orderHistory.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(orderDto.getOrderHistory()).usingRecursiveComparison().isEqualTo(orderHistory);
            softAssertions.assertThat(orderDto.getProducts()).hasSize(1);
            softAssertions.assertThat(orderDto.getProducts().get(0)).usingRecursiveComparison().isEqualTo(orderedProductDto);
        });
    }

    @Nested
    class addOrderItem_테스트 {
        @Test
        void 유저가_주문을_한다() {
            // given
            final Member Dooly = Member.of(1L, "dooly@dooly.com", "1234", 100000L);
            final OrderCartItemDto orderCartItemDto = OrderCartItemDto.of(Member_Dooly_CartItem1.ID, CHICKEN.NAME, CHICKEN.PRICE, CHICKEN.IMAGE_URL);
            final OrderHistory orderHistory = new OrderHistory(1L, Dooly, 100000L, LocalDateTime.now());

            when(orderHistoryDao.save(any(OrderHistory.class))).thenReturn(1L);
            when(cartItemDao.findById(Member_Dooly_CartItem1.ID)).thenReturn(Member_Dooly_CartItem1.ENTITY);
            when(orderHistoryDao.findById(orderHistory.getId())).thenReturn(orderHistory);
            doNothing().when(cartItemDao).deleteById(any());
            doNothing().when(memberDao).updateMember(Dooly);

            // when
            final Long orderHistoryId = orderService.addOrderHistory(Dooly, List.of(orderCartItemDto));

            // then
            assertThat(orderHistoryId).isEqualTo(1L);
        }

        @Test
        void 상품_주문_도중에_상품_정보가_바뀌면_예외를_반환하다() {
            // given
            final Member Dooly = Member.of(1L, "dooly@dooly.com", "1234", 100000L);
            final OrderCartItemDto orderCartItemDto = OrderCartItemDto.of(Member_Dooly_CartItem1.ID, CHICKEN.NAME, CHICKEN.PRICE, CHICKEN.IMAGE_URL);
            final OrderHistory orderHistory = new OrderHistory(1L, Dooly, 100000L, LocalDateTime.now());

            when(orderHistoryDao.save(any(OrderHistory.class))).thenReturn(orderHistory.getId());
            when(cartItemDao.findById(Member_Dooly_CartItem1.ID)).thenReturn(Member_Dooly_CartItem2.ENTITY);

            // when, then
            assertThatThrownBy(() -> orderService.addOrderHistory(Dooly, List.of(orderCartItemDto)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품 정보가 업데이트 되었습니다. 다시 확인해주세요");
        }
    }
}
