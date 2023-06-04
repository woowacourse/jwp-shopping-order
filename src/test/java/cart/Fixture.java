package cart;

import cart.domain.*;
import cart.domain.pointmanager.DefaultPointManager;

import java.util.List;

public class Fixture {


    public static final Member memberA = new Member(1L, "a@a.com", "1234");
    public static final Product productA = new Product(1L, "A", 1000, "http://image.com");
    public static final CartItem cartItem1 = new CartItem(1L, 3, productA, memberA);
    public static final OrderCheckout orderCheckout1 = OrderCheckout.of(1000, List.of(cartItem1), new DefaultPointManager());
    public static final OrderItem orderItem1 = new OrderItem(1L, "A", 1000, "http://image.com", 1);
    public static final Order order1 = new Order(1L, 1L, List.of(orderItem1), 1000, 900, 100, 100, "2023-05-29 08:55:03");
    public static final Order order2 = new Order(2L, 1L, List.of(orderItem1), 1000, 1000, 100, 0, "2023-05-31 06:49:14");


}
