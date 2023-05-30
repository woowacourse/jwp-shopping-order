package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.entity.MemberEntity;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.OrderProductRecordEntity;
import cart.dao.entity.ProductEntity;
import cart.test.RepositoryTest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderProductRecordDaoTest {

    @Autowired
    private OrderProductRecordDao orderProductRecordDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderProductDao orderProductDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("save 메서드는 상품과 주문 상품의 연결 기록을 저장한다.")
    void save() {
        MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 10);
        Long memberId = memberDao.addMember(memberEntity);
        ProductEntity productEntity = new ProductEntity("치킨", 10000, "http://chicken.com");
        Long productId = productDao.createProduct(productEntity);
        OrderEntity orderEntity = new OrderEntity(memberEntity.assignId(memberId), 0);
        Long orderId = orderDao.save(orderEntity);
        OrderProductEntity orderProductEntity =
                new OrderProductEntity(orderId, productEntity.assignId(productId), "치킨", 10000, "http://chicken.com", 5);
        Long orderProductId = orderProductDao.save(orderProductEntity);

        OrderProductRecordEntity orderProductRecordEntity = new OrderProductRecordEntity(orderProductId, productId);
        Long orderProductRecordId = orderProductRecordDao.save(orderProductRecordEntity);

        Optional<OrderProductRecordEntity> result = orderProductRecordDao.findById(orderProductRecordId);
        assertThat(result).isNotEmpty();
    }
}
