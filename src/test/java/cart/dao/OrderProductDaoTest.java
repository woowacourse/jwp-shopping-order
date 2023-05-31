package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.entity.MemberEntity;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.ProductEntity;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderProductDaoTest {

    private MemberEntity member;
    private ProductEntity product;
    private OrderEntity order;
    private OrderProductEntity orderProductA;
    private OrderProductEntity orderProductB;

    @Autowired
    private OrderProductDao orderProductDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        member = new MemberEntity("a@a.com", "password1", 10);
        Long memberId = memberDao.addMember(member);
        member = member.assignId(memberId);

        product = new ProductEntity("치킨", 10000, "http://chicken.com");
        Long productId = productDao.createProduct(product);
        product = product.assignId(productId);

        order = new OrderEntity(member, 10, 0);
        Long orderId = orderDao.save(order);
        order = order.assignId(orderId);

        orderProductA =
                new OrderProductEntity(orderId, product, "치킨", 13000, "http://chicken.com", 10);
        Long orderProductIdA = orderProductDao.save(orderProductA);
        orderProductA = orderProductA.assignId(orderProductIdA);

        orderProductB =
                new OrderProductEntity(orderId, product, "치킨", 13000, "http://chicken.com", 10);
        Long orderProductIdB = orderProductDao.save(orderProductB);
        orderProductB = orderProductB.assignId(orderProductIdB);
    }

    @Nested
    @DisplayName("findAllByOrderId 메서드는 ")
    class FindAllByOrderId {

        @Test
        @DisplayName("주문의 모든 주문 상품 정보를 조회한다.")
        void allOrderProduct() {
            List<OrderProductEntity> result = orderProductDao.findAllByOrderId(order.getId());

            assertAll(
                    () -> assertThat(result).hasSize(2),
                    () -> assertThat(result.get(0)).usingRecursiveComparison()
                            .ignoringExpectedNullFields()
                            .isEqualTo(orderProductA),
                    () -> assertThat(result.get(1)).usingRecursiveComparison()
                            .ignoringExpectedNullFields()
                            .isEqualTo(orderProductB)
            );
        }

        @Test
        @DisplayName("주문할 때 상품의 기록이 남아있으면 상품 정보도 함께 조회한다.")
        void orderProductWithProduct() {
            List<OrderProductEntity> result = orderProductDao.findAllByOrderId(order.getId());

            assertAll(
                    () -> assertThat(result).hasSize(2),
                    () -> assertThat(result.get(0)).usingRecursiveComparison()
                            .ignoringExpectedNullFields()
                            .isEqualTo(orderProductA),
                    () -> assertThat(result.get(1)).usingRecursiveComparison()
                            .ignoringExpectedNullFields()
                            .isEqualTo(orderProductB)
            );
        }
    }

    @Test
    @DisplayName("saveAll 메서드는 모든 주문 상품을 저장한다.")
    void saveAll() {
        OrderProductEntity orderProductC =
                new OrderProductEntity(order.getId(), product, "치킨", 13000, "http://chicken.com", 10);
        OrderProductEntity orderProductD =
                new OrderProductEntity(order.getId(), product, "치킨", 13000, "http://chicken.com", 10);

        int result = orderProductDao.saveAll(List.of(orderProductC, orderProductD));

        assertThat(result).isEqualTo(2);
    }
}
