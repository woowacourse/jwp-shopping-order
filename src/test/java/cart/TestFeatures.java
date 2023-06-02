package cart;

import cart.domain.*;
import cart.domain.point.Point;
import cart.domain.point.UsedPoint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestFeatures {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final Product 상품1_치킨 = new Product(1L, "치킨", 10_000, "testImage1");
    public static final Product 상품2_샐러드 = new Product(2L, "샐러드", 20_000, "testImage2");
    public static final Product 상품3_피자 = new Product(3L, "피자", 13_000, "testImage3");

    public static final Member 회원1 = new Member(1L, "a@a.com", "1234");
    public static final Member 회원2 = new Member(2L, "b@b.com", "1234");

    public static final CartItem 회원1_장바구니1 = new CartItem(1L, 2, 상품1_치킨, 회원1);
    public static final CartItem 회원1_장바구니2 = new CartItem(1L, 4, 상품2_샐러드, 회원1);
    public static final CartItem 회원2_장바구니1 = new CartItem(1L, 5, 상품3_피자, 회원2);

    public static final PurchaseOrderItem 주문1_상품1 = new PurchaseOrderItem(1L, 상품1_치킨, 2);
    public static final PurchaseOrderItem 주문1_상품2 = new PurchaseOrderItem(2L, 상품2_샐러드, 4);
    public static final PurchaseOrderItem 주문2_상품1 = new PurchaseOrderItem(3L, 상품1_치킨, 5);
    public static final PurchaseOrderItem 주문2_상품2 = new PurchaseOrderItem(4L, 상품3_피자, 3);
    public static final PurchaseOrderItem 주문3_상품1 = new PurchaseOrderItem(5L, 상품3_피자, 5);
    public static final PurchaseOrderItem 주문3_상품2 = new PurchaseOrderItem(6L, 상품2_샐러드, 7);

    public static final PurchaseOrderInfo 회원1_주문1 = new PurchaseOrderInfo(1L, 회원1, LocalDateTime.parse("2023-05-20 12:12:12", formatter), 10_000, 1_000, OrderStatus.PENDING);
    public static final PurchaseOrderInfo 회원1_주문2 = new PurchaseOrderInfo(2L, 회원1, LocalDateTime.parse("2023-05-25 14:13:12", formatter), 12_000, 4_000, OrderStatus.CANCELLED);
    public static final PurchaseOrderInfo 회원1_주문3 = new PurchaseOrderInfo(3L, 회원1, LocalDateTime.parse("2023-05-31 17:11:12", formatter), 21_000, 2_000, OrderStatus.PENDING);

    public static final Point 회원1_주문1_포인트 = new Point(1L, 500, LocalDateTime.parse("2023-05-20 12:12:12", formatter), LocalDateTime.parse("2023-05-25 12:12:12", formatter));
    public static final Point 회원1_주문2_포인트 = new Point(2L, 700, LocalDateTime.parse("2023-05-18 12:12:12", formatter), LocalDateTime.parse("2023-05-29 12:12:12", formatter));
    public static final Point 회원1_주문3_포인트 = new Point(3L, 1000, LocalDateTime.parse("2023-05-15 12:12:12", formatter), LocalDateTime.parse("2023-05-30 12:12:12", formatter));

    public static final UsedPoint 주문1_포인트_사용1 = new UsedPoint(1L, 1L, 500);
    public static final UsedPoint 주문1_포인트_사용2 = new UsedPoint(2L, 2L, 400);
    public static final UsedPoint 주문1_포인트_사용3 = new UsedPoint(3L, 3L, 800);
}
