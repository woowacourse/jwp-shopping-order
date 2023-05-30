package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.entity.MemberEntity;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.OrderProductRecordEntity;
import cart.dao.entity.ProductEntity;
import cart.test.RepositoryTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderProductDaoTest {

    @Autowired
    private OrderProductDao orderProductDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderProductRecordDao orderProductRecordDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MemberDao memberDao;

    @Nested
    @DisplayName("findAllByOrderId 메서드는 ")
    class FindAllByOrderId {

        @Test
        @DisplayName("주문의 모든 주문 상품 정보를 조회한다.")
        void allOrderProduct() {
            MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 10);
            Long memberId = memberDao.addMember(memberEntity);

            OrderEntity orderEntity = new OrderEntity(memberEntity.assignId(memberId), 10);
            Long orderId = orderDao.save(orderEntity);

            OrderProductEntity orderProductEntityA =
                    new OrderProductEntity(orderId, null, "치킨", 13000, "http://chicken.com", 10);
            Long orderProductIdA = orderProductDao.save(orderProductEntityA);

            OrderProductEntity orderProductEntityB =
                    new OrderProductEntity(orderId, null, "치킨", 13000, "http://chicken.com", 10);
            Long orderProductIdB = orderProductDao.save(orderProductEntityB);

            List<OrderProductEntity> result = orderProductDao.findAllByOrderId(orderId);

            assertAll(
                    () -> assertThat(result).hasSize(2),
                    () -> assertThat(result.get(0)).usingRecursiveComparison()
                            .ignoringFieldsOfTypes(LocalDateTime.class)
                            .isEqualTo(orderProductEntityA.assignId(orderProductIdA)),
                    () -> assertThat(result.get(1)).usingRecursiveComparison()
                            .ignoringFieldsOfTypes(LocalDateTime.class)
                            .isEqualTo(orderProductEntityB.assignId(orderProductIdB))
            );
        }

        @Test
        @DisplayName("주문할 때 상품의 기록이 남아있으면 상품 정보도 함께 조회한다.")
        void orderProductWithProduct() {
            MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 10);
            Long memberId = memberDao.addMember(memberEntity);

            ProductEntity productEntity = new ProductEntity("치킨", 10000, "http://chicken.com");
            Long productId = productDao.createProduct(productEntity);

            OrderEntity orderEntity = new OrderEntity(memberEntity.assignId(memberId), 10);
            Long orderId = orderDao.save(orderEntity);

            OrderProductEntity orderProductEntityA =
                    new OrderProductEntity(orderId, productEntity.assignId(productId), "치킨", 13000, "http://chicken.com", 10);
            Long orderProductIdA = orderProductDao.save(orderProductEntityA);

            OrderProductEntity orderProductEntityB =
                    new OrderProductEntity(orderId, productEntity.assignId(productId), "치킨", 13000, "http://chicken.com", 10);
            Long orderProductIdB = orderProductDao.save(orderProductEntityB);

            orderProductRecordDao.save(new OrderProductRecordEntity(orderProductIdA, productId));
            orderProductRecordDao.save(new OrderProductRecordEntity(orderProductIdB, productId));

            List<OrderProductEntity> result = orderProductDao.findAllByOrderId(orderId);

            assertAll(
                    () -> assertThat(result).hasSize(2),
                    () -> assertThat(result.get(0)).usingRecursiveComparison()
                            .ignoringFieldsOfTypes(LocalDateTime.class)
                            .isEqualTo(orderProductEntityA.assignId(orderProductIdA)),
                    () -> assertThat(result.get(1)).usingRecursiveComparison()
                            .ignoringFieldsOfTypes(LocalDateTime.class)
                            .isEqualTo(orderProductEntityB.assignId(orderProductIdB))
            );
        }
    }
}
