package cart;

import static java.util.Base64.getEncoder;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponType;
import cart.domain.order.Order;
import cart.domain.product.CartItem;
import cart.domain.product.Product;
import cart.domain.vo.Price;
import cart.domain.vo.Quantity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class TestDataFixture {

    //Product
    public static final Product PRODUCT_1 = new Product(1L, "치킨", new Price(10000),
            "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static final Product PRODUCT_2 = new Product(2L, "샐러드", new Price(20000),
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static final Product PRODUCT_3 = new Product(3L, "피자", new Price(13000),
            "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80");

    //Member
    public static final Member MEMBER_1 = new Member(1L, "a@a.com", "1234");
    public static final Member MEMBER_2 = new Member(2L, "b@b.com", "1234");
    public static final Member MEMBER_3 = new Member(3L, "sangun", "1234");
    public static final Member MEMBER_4 = new Member(4L, "lopi", "1234");

    //Cart-item
    public static final CartItem CART_ITEM_1 = new CartItem(1L, new Quantity(2), PRODUCT_1, MEMBER_1.getId());
    public static final CartItem CART_ITEM_2 = new CartItem(2L, new Quantity(4), PRODUCT_2, MEMBER_1.getId());

    //Order
    public static final Order ORDER_NO_USE_COUPON = Order.of(MEMBER_1, List.of(CART_ITEM_1));

    //Coupon
    public static final Coupon DISCOUNT_50_PERCENT
            = new Coupon(null, "50% 할인 쿠폰", 50, CouponType.RATE_DISCOUNT);
    public static final Coupon DISCOUNT_5000_CONSTANT
            = new Coupon(null, "5000원 할인 쿠폰", 50, CouponType.CONSTANT_DISCOUNT);

    //AuthHeader
    public static final String MEMBER_1_AUTH_HEADER =
            "Basic " + new String(getEncoder().encode(
                    String.format("%s:%s", MEMBER_1.getEmail(), MEMBER_1.getPassword()).getBytes()));

    //ObjectMapper
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String objectToJsonString(final Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
