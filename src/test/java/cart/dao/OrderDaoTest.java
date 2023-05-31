package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.Products;
import cart.domain.vo.Amount;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

@JdbcTest(includeFilters = {
    @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
class OrderDaoTest {

    private final Member member = new Member(1L, "test@test.com", "password");
    private final Coupon coupon = new Coupon("name", Amount.of(1_000), Amount.of(10_000), false);

    @Autowired
    private CouponDao couponDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDao orderDao;

    @Test
    @DisplayName("주문을 저장한다.")
    void testSave() {
        //given
        final Member savedMember = memberDao.addMember(member);
        final Coupon savedCoupon = couponDao.save(coupon, savedMember.getId());
        final Product product1 = new Product("product1", Amount.of(10_000), "imageUrl1");
        final Product product2 = new Product("product2", Amount.of(20_000), "imageUrl2");
        final Long product1Id = productDao.createProduct(product1);
        final Long product2Id = productDao.createProduct(product2);
        final Product savedProduct1 = new Product(product1Id, "product1", Amount.of(10_000), "imageUrl1");
        final Product savedProduct2 = new Product(product2Id, "product2", Amount.of(20_000), "imageUrl2");
        final Order order = new Order(new Products(List.of(savedProduct1, savedProduct2)), savedCoupon,
            Amount.of(3_000),
            "address");

        //when
        final Order savedOrder = orderDao.save(order, savedMember.getId());

        //then
        assertThat(savedOrder.getId()).isNotNull();
    }
}
