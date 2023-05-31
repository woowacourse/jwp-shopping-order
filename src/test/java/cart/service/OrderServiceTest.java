package cart.service;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Point;
import cart.domain.PointPolicy;
import cart.domain.common.Money;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.OrderInfo;
import cart.dto.OrderRequest;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cart.common.fixture.DomainFixture.EMAIL;
import static cart.common.fixture.DomainFixture.PASSWORD;
import static cart.common.fixture.DomainFixture.PRODUCT_IMAGE;
import static cart.common.fixture.DomainFixture.PRODUCT_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;


    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private CartItemDao cartItemDao;
    @Mock
    private ProductDao productDao;
    @Mock
    private PointPolicy pointPolicy;

    @Test
    void 주문을_추가한다() {
        //given
        final Product product = new Product(1L, PRODUCT_NAME, 20000, PRODUCT_IMAGE);
        final Member member = new Member(1L, EMAIL, PASSWORD, 1000);
        final OrderRequest orderRequest = new OrderRequest(List.of(new OrderInfo(product.getId(), 1)), 19000, 1000);

        when(cartItemDao.findByMemberId(member.getId()))
                .thenReturn(List.of(new CartItem(member, product)));

        when(productDao.getProductById(product.getId()))
                .thenReturn(product);

        when(pointPolicy.save(Money.valueOf(19000)))
                .thenReturn(Point.valueOf(1900));

        when(orderRepository.addOrder(Order.from(member, 19000, 1000, List.of(new OrderItem(product, 1)))))
                .thenReturn(1L);

        //when
        final Long id = orderService.addOrder(member, orderRequest);

        //then
        assertThat(id).isEqualTo(1L);
        verify(memberRepository, times(1)).updateMemberPoint(new Member(1L, EMAIL, PASSWORD, 1900));
    }
}
