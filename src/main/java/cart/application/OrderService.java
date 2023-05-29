package cart.application;

import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.Product;
import cart.dto.OrderResponse;
import cart.dto.ProductInOrderResponse;
import cart.exception.AuthenticationException;
import cart.exception.NoSuchDataExistException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;

    public OrderService(final OrderDao orderDao, final OrderProductDao orderProductDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
    }

    public OrderResponse findOrderById(final Member member, final Long orderId) {
        final Order order = orderDao.findById(orderId)
                .orElseThrow(NoSuchDataExistException::new);
        if (!order.getMember().matchMemberByInfo(member)) {
            throw new AuthenticationException();
        }

        final List<OrderProduct> orderProducts = orderProductDao.findByOrderId(orderId);

        final List<ProductInOrderResponse> productInOrderResponses = orderProducts.stream()
                .map(this::toProductInOrderResponse)
                .collect(Collectors.toList());
        return toOrderResponse(order, productInOrderResponses);
    }

    private ProductInOrderResponse toProductInOrderResponse(final OrderProduct orderProduct) {
        final Product product = orderProduct.getProduct();
        return new ProductInOrderResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                orderProduct.getQuantity(),
                product.getImageUrl()
        );
    }

    private OrderResponse toOrderResponse(final Order order, final List<ProductInOrderResponse> productsResponses) {
        return new OrderResponse(order.getTotalPrice(), order.getFinalPrice(), productsResponses);
    }
}
