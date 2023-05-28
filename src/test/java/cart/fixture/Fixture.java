package cart.fixture;

import cart.domain.Member;
import cart.domain.Orders;

import java.util.List;

public class Fixture {
    public static final String EMAIL = "a@a.com";
    public static final String PASSWORD = "1234";
    public static final Member TEST_MEMBER = new Member(1L,EMAIL,PASSWORD);
    public static final Orders ORDERS = new Orders(1L, List.of(1L,2L,3L),1000,900,1L);
}
