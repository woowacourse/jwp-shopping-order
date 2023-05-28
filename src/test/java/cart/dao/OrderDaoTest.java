package cart.dao;

import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.orderproduct.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import({OrderDao.class, MemberDao.class})
public class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;

    @Test
    void save() {
        // given
        final Member member = memberDao.getMemberById(1L);

        // when
        final Order order = new Order(member, new MemberPoint(1000));
        final Long orderId = orderDao.save(order);

        // then
        // TODO: 서비스에서 orders와 order_product에 각각 쿼리를 날려야 하나? 
        // 아니며 레포지토리를 하나 만들어서 각각의 dao에 쿼리를 남기도록 관심사를 분리해야 하나
    }
}
