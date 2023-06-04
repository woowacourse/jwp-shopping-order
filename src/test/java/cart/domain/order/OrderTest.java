package cart.domain.order;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import cart.domain.cart.CartItem;
import cart.domain.coupon.type.FixedAmount;
import cart.domain.coupon.type.NotUsed;
import cart.domain.coupon.type.Percentage;
import cart.domain.monetary.DeliveryFee;
import cart.domain.monetary.Discount;
import cart.domain.product.Product;

class OrderTest {

	private List<OrderItem> orderItems;
	private BigDecimal totalPrice;

	@BeforeEach
	public void setUp() {
		orderItems = List.of(
			new OrderItem(new CartItem(1L, new Product("NameA", BigDecimal.valueOf(1000L), "imageUrl1"), 2)),
			new OrderItem(new CartItem(2L, new Product("NameB", BigDecimal.valueOf(2000L), "imageUrl2"), 1)),
			new OrderItem(new CartItem(3L, new Product("NameC", BigDecimal.valueOf(3000L), "imageUrl3"), 4)),
			new OrderItem(new CartItem(4L, new Product("NameD", BigDecimal.valueOf(4000L), "imageUrl4"), 5))
		);
		totalPrice = BigDecimal.valueOf(1000L * 2 + 2000L * 1 + 3000L * 4 + 4000L * 5);
	}

	@Test
	void isSameTotalPrice() {
		// given
		final Order order = new Order(null, orderItems, new NotUsed(), new DeliveryFee(), OrderStatus.PAID,
			LocalDateTime.now());

		// when
		final boolean actual = order.isSameTotalPrice(totalPrice);

		// then
		assertThat(actual).isTrue();
	}

	@Test
	void isDifferentTotalPrice() {
		// given
		final Order order = new Order(null, orderItems, new NotUsed(), new DeliveryFee(), OrderStatus.PAID,
			LocalDateTime.now());
		// when
		final boolean actual = order.isDifferentTotalPrice(totalPrice.subtract(BigDecimal.ONE));

		// then
		assertThat(actual).isTrue();
	}

	@Test
	void calculateTotalPrice() {
		// given
		final Order order = new Order(null, orderItems, new NotUsed(), new DeliveryFee(), OrderStatus.PAID,
			LocalDateTime.now());
		// when
		final BigDecimal actual = order.calculateTotalPrice();

		// then
		assertThat(actual).isEqualTo(totalPrice);
	}

	@Nested
	class CalculateTotalPaymentsTest {
		@Test
		void usePercentageCoupon() {
			// given
			final BigDecimal discountPercentage = BigDecimal.valueOf(15);
			final Percentage couponInfo = new Percentage(1L, "15% 할인 쿠폰", new Discount(discountPercentage));
			final DeliveryFee deliveryFee = new DeliveryFee();
			final OrderStatus paid = OrderStatus.PAID;
			final Order order = new Order(null, orderItems, couponInfo, deliveryFee, paid, LocalDateTime.now());

			// when
			final BigDecimal actual = order.calculateTotalPayments();

			final BigDecimal discountedTotalPrice = totalPrice.multiply(BigDecimal.ONE.subtract(
				discountPercentage.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN)));

			// then
			assertThat(actual).isEqualTo(discountedTotalPrice.add(deliveryFee.getAmount()));
		}

		@Test
		void useFixedAmountCoupon() {
			// given
			final BigDecimal discountAmount = BigDecimal.valueOf(5000);
			final FixedAmount couponInfo = new FixedAmount(2L, "가입기념 5000원 할인 쿠폰", new Discount(discountAmount));
			final DeliveryFee deliveryFee = new DeliveryFee();
			final OrderStatus paid = OrderStatus.PAID;
			final Order order = new Order(null, orderItems, couponInfo, deliveryFee, paid, LocalDateTime.now());

			// when
			final BigDecimal actual = order.calculateTotalPayments();

			// then
			assertThat(actual).isEqualTo(totalPrice.subtract(discountAmount).add(deliveryFee.getAmount()));
		}

		@Test
		void notUsed() {
			// given
			final NotUsed couponInfo = new NotUsed();
			final DeliveryFee deliveryFee = new DeliveryFee();
			final OrderStatus paid = OrderStatus.PAID;
			final Order order = new Order(null, orderItems, couponInfo, deliveryFee, paid, LocalDateTime.now());

			// when
			final BigDecimal actual = order.calculateTotalPayments();

			// then
			assertThat(actual).isEqualTo(totalPrice.add(deliveryFee.getAmount()));
		}

	}

}
