package cart.fixture.entity;

import cart.dao.entity.OrderEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class OrderEntityFixture {

    public static OrderEntity 주문_엔티티(Long 회원_식별자값, String 총금액, String 사용한_포인트, LocalDateTime 주문_시간) {
        return new OrderEntity(회원_식별자값, new BigDecimal(총금액), new BigDecimal(사용한_포인트), 주문_시간);
    }
}
