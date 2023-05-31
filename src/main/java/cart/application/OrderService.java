package cart.application;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.Products;
import cart.domain.vo.Amount;
import cart.dto.CartItemRequest;
import cart.dto.OrderProductResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.BusinessException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public OrderService(final OrderDao orderDao, final ProductDao productDao, final CouponDao couponDao,
        final MemberCouponDao memberCouponDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public OrderResponse order(final OrderRequest orderRequest, final Member member) {
        final List<Product> products = findProducts(orderRequest);
        final Coupon coupon = couponDao.findById(orderRequest.getCouponId(), member.getId())
            .orElseThrow(() -> new BusinessException("존재하지 않는 쿠폰입니다."));
        final Order order = orderDao.save(
            new Order(new Products(products), coupon, Amount.of(orderRequest.getDeliveryAmount()),
                orderRequest.getAddress()));
        final Coupon usedCoupon = coupon.use();
        couponDao.update(usedCoupon);
        final List<OrderProductResponse> orderProductResponses = makeOrderProductResponses(orderRequest,
            products);
        return new OrderResponse(order.getId(), orderRequest.getTotalAmount(),
            order.discountDeliveryAmount().getValue(), order.discountProductAmount().getValue(), order.getAddress(),
            orderProductResponses);
    }

    private List<Product> findProducts(final OrderRequest orderRequest) {
        final List<Product> products = new ArrayList<>();
        final List<Amount> amounts = orderRequest.getProducts().stream()
            .map(it -> {
                final Product product = productDao.getProductById(it.getProductId());
                products.add(product);
                return product.getAmount().multiply(it.getQuantity());
            })
            .collect(Collectors.toList());
        final Amount sum = Amount.of(amounts);
        if (!sum.equals(Amount.of(orderRequest.getTotalAmount()))) {
            throw new BusinessException("상품 금액이 변경되었습니다.");
        }
        return products;
    }

    private List<OrderProductResponse> makeOrderProductResponses(final OrderRequest orderRequest,
        final List<Product> products) {
        final List<OrderProductResponse> orderProductResponses = new ArrayList<OrderProductResponse>();
        for (int index = 0; index < products.size(); index++) {
            final Product product = products.get(index);
            final CartItemRequest cartItemRequest = orderRequest.getProducts().get(index);
            final OrderProductResponse orderProductResponse = new OrderProductResponse(product.getId(),
                product.getName(), product.getAmount().getValue(),
                product.getImageUrl(), cartItemRequest.getQuantity());
            orderProductResponses.add(orderProductResponse);
        }
        return orderProductResponses;
    }
}
