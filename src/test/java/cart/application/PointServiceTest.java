package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import cart.application.event.UpdateMemberPointEvent;
import cart.domain.cart.Quantity;
import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Password;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.point.Point;
import cart.domain.product.ImageUrl;
import cart.domain.product.Name;
import cart.domain.product.Price;
import cart.domain.product.Product;
import cart.dto.request.OrderRequest;
import cart.repository.OrderRepository;
import cart.repository.PointRepository;
import java.sql.Timestamp;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    private PointService pointService;

    @Mock
    private PointRepository pointRepository;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        pointService = new PointService(pointRepository, orderRepository);
    }

    @Test
    void 포인트로_결제한_것_이외의_상품_가격에서_포인트를_기존_멤버의_포인트에_누적한다() {
        // given
        final Member member = new Member(1L, new Email("a@a.com"), new Password("1234"));
        final OrderRequest orderRequest = new OrderRequest(List.of(1L), 500L);
        final OrderItem orderItem = new OrderItem(
                1L,
                new Quantity(1),
                new Product(1L, new Name("상품"), new ImageUrl("image.com"), new Price(10000L))
        );
        final Order order = new Order(
                1L,
                member,
                new Timestamp(System.currentTimeMillis()),
                List.of(orderItem)
        );
        final Point memberPoint = new Point(1000L);

        given(pointRepository.findPointByMember(member)).willReturn(memberPoint);
        given(orderRepository.findByOrderId(1L)).willReturn(order);

        // when
        pointService.updateMemberPoint(new UpdateMemberPointEvent(member, orderRequest, 1L));

        //then
        ArgumentCaptor<Member> memberArgumentCaptor = ArgumentCaptor.forClass(Member.class);
        ArgumentCaptor<Point> pointArgumentCaptor = ArgumentCaptor.forClass(Point.class);
        verify(pointRepository).updateMemberPoint(memberArgumentCaptor.capture(), pointArgumentCaptor.capture());
        assertAll(
                () -> assertThat(memberArgumentCaptor.getValue().getId()).isEqualTo(1L),
                () -> assertThat(pointArgumentCaptor.getValue().getPoint()).isEqualTo(738L)
        );
    }

    @Test
    void 멤버가_주문에_사용한_포인트와_해당_주문으로_누적된_포인트_이력을_저장한다() {
        // given
        final Member member = new Member(1L, new Email("a@a.com"), new Password("1234"));
        final OrderRequest orderRequest = new OrderRequest(List.of(1L), 500L);
        final OrderItem orderItem = new OrderItem(
                1L,
                new Quantity(1),
                new Product(1L, new Name("상품"), new ImageUrl("image.com"), new Price(10000L))
        );
        final Order order = new Order(
                1L,
                member,
                new Timestamp(System.currentTimeMillis()),
                List.of(orderItem)
        );
        final Point memberPoint = new Point(1000L);
        //9500  238 saved
        given(pointRepository.findPointByMember(member)).willReturn(memberPoint);
        given(orderRepository.findByOrderId(1L)).willReturn(order);

        // when
        pointService.updateMemberPoint(new UpdateMemberPointEvent(member, orderRequest, 1L));

        //then
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        ArgumentCaptor<Point> usePointArgumentCaptor = ArgumentCaptor.forClass(Point.class);
        ArgumentCaptor<Point> savedPointArgumentCaptor = ArgumentCaptor.forClass(Point.class);
        verify(pointRepository).updatePointHistory(
                orderArgumentCaptor.capture(),
                usePointArgumentCaptor.capture(),
                savedPointArgumentCaptor.capture()
        );

        assertAll(
                () -> assertThat(orderArgumentCaptor.getValue().getId()).isEqualTo(1L),
                () -> assertThat(usePointArgumentCaptor.getValue().getPoint()).isEqualTo(500L),
                () -> assertThat(savedPointArgumentCaptor.getValue().getPoint()).isEqualTo(238L)
        );
    }
}
