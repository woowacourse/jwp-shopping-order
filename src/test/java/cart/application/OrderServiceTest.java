package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.member.Member;
import cart.exception.OrderException;
import cart.repository.mapper.MemberMapper;
import cart.test.ServiceTest;
import cart.ui.controller.dto.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderProductDao orderProductDao;

    @Nested
    @DisplayName("getOrderDetail 메서드는 ")
    class GetOrderDetail {

        private MemberEntity memberEntity;
        private ProductEntity productEntity;
        private OrderEntity orderEntity;
        private OrderProductEntity orderProductEntity;

        @BeforeEach
        void setUp() {
            memberEntity = new MemberEntity("a@a.com", "password1", 0);
            Long memberId = memberDao.addMember(memberEntity);
            memberEntity = memberEntity.assignId(memberId);

            productEntity = new ProductEntity("치킨", 10000, "http://chicken.com");
            Long productId = productDao.createProduct(productEntity);
            productEntity = productEntity.assignId(productId);

            orderEntity = new OrderEntity(memberEntity, 0, 0);
            Long orderId = orderDao.save(orderEntity);
            orderEntity = orderEntity.assignId(orderId);

            orderProductEntity =
                    new OrderProductEntity(orderId, null, "치킨", 10000, "http://chicken.com", 5);
            Long orderProductId = orderProductDao.save(orderProductEntity);
            orderProductEntity.assignId(orderProductId);
        }

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

            System.out.println(response.getOrderedAt());
            assertAll(
                    () -> assertThat(response.getOrderId()).isEqualTo(orderEntity.getId()),
                    () -> assertThat(response.getProducts()).hasSize(1),
                    () -> assertThat(response.getProducts().get(0).getProductId()).isEqualTo(null),
                    () -> assertThat(response.getProducts().get(0).getName()).isEqualTo(productEntity.getName()),
                    () -> assertThat(response.getProducts().get(0).getPrice()).isEqualTo(productEntity.getPrice()),
                    () -> assertThat(response.getProducts().get(0).getImageUrl()).isEqualTo(productEntity.getImageUrl()),
                    () -> assertThat(response.getProducts().get(0).getQuantity()).isEqualTo(orderProductEntity.getQuantity()),
                    () -> assertThat(response.getTotalPrice())
                            .isEqualTo(productEntity.getPrice() * orderProductEntity.getQuantity()),
                    () -> assertThat(response.getUsedPoint()).isEqualTo(orderEntity.getUsedPoint()),
                    () -> assertThat(response.getDeliveryFee()).isEqualTo(orderEntity.getDeliveryFee()),
                    () -> assertThat(response.getOrderedAt()).isNotNull()
            );
        }
    }
}
