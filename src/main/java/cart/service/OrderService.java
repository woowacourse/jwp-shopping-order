package cart.service;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.OrderEntity;
import cart.domain.OrderItemEntity;
import cart.domain.Point;
import cart.domain.Product;
import cart.dto.CartItemInfoRequest;
import cart.dto.OrderItemResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.ProductInfoRequest;
import cart.dto.ProductResponse;
import cart.exception.IllegalUsePointException;
import cart.exception.InsufficientStockException;
import cart.exception.MismatchedTotalFeeException;
import cart.exception.MismatchedTotalPriceException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.util.CurrentTimeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final ProductDao productDao;
    private final CartItemRepository cartItemRepository;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public long order(final Member member, final OrderRequest orderRequest) {
        validateOwner(member, orderRequest);
        validatePrice(orderRequest);
        validatePoint(orderRequest);
        validateFee(orderRequest);

        removeCartItem(orderRequest);

        savePoint(member, orderRequest);

        updateStock(orderRequest);

        return saveOrderData(member, orderRequest);
    }

    private void validateOwner(final Member member, final OrderRequest orderRequest) {
        List<CartItemInfoRequest> cartItemInfos = orderRequest.getCartItemInfos();

        for (final CartItemInfoRequest cartItemInfo : cartItemInfos) {
            CartItem cartItem = cartItemRepository.findById(cartItemInfo.getCartItemId());

            cartItem.validateOwner(member);
        }
    }

    private void validatePrice(final OrderRequest orderRequest) {
        List<CartItemInfoRequest> cartItemInfos = orderRequest.getCartItemInfos();

        int totalPrice = 0;

        for (final CartItemInfoRequest cartItemInfo : cartItemInfos) {
            ProductInfoRequest productInfo = cartItemInfo.getProductInfo();

            Long productId = productInfo.getProductId();
            Product product = productDao.findById(productId);
            totalPrice += product.getPrice();
        }

        if (totalPrice != orderRequest.getTotalProductPrice()) {
            throw new MismatchedTotalPriceException();
        }
    }

    private void validatePoint(final OrderRequest orderRequest) {
        Integer usePoint = orderRequest.getUsePoint();
        
        if (usePoint > 0 && usePoint < Point.MIN_USAGE_VALUE) {
            throw new IllegalUsePointException();
        }
    }

    private void validateFee(final OrderRequest orderRequest) {
        Integer totalProductPrice = orderRequest.getTotalProductPrice();
        Integer totalDeliveryFee = orderRequest.getTotalDeliveryFee();
        Integer totalPrice = orderRequest.getTotalPrice();

        if (totalProductPrice + totalDeliveryFee != totalPrice) {
            throw new MismatchedTotalFeeException();
        }
    }

    private void removeCartItem(final OrderRequest orderRequest) {
        List<CartItemInfoRequest> cartItemInfos = orderRequest.getCartItemInfos();

        for (final CartItemInfoRequest cartItemInfo : cartItemInfos) {
            cartItemRepository.deleteById(cartItemInfo.getCartItemId());
        }
    }

    private void savePoint(final Member member, final OrderRequest orderRequest) {
        Integer totalProductPrice = orderRequest.getTotalProductPrice();

        int point = (int) Math.ceil(totalProductPrice * 0.05);

        Member newMember = new Member(member.getId(), member.getEmail(), member.getPassword(), member.getPointValue() + point);
        memberRepository.update(newMember);
    }

    private void updateStock(final OrderRequest orderRequest) {
        List<CartItemInfoRequest> cartItemInfos = orderRequest.getCartItemInfos();

        for (final CartItemInfoRequest cartItemInfo : cartItemInfos) {
            Integer quantity = cartItemInfo.getQuantity();

            ProductInfoRequest productInfo = cartItemInfo.getProductInfo();
            Long productId = productInfo.getProductId();
            Integer stock = productInfo.getStock();

            validateStock(productId, quantity);

            productDao.updateStockById(productId, stock - quantity);
        }
    }

    private void validateStock(final Long productId, final Integer quantity) {
        Product product = productDao.findById(productId);

        int stock = product.getStock();

        if (stock - quantity < 0) {
            throw new InsufficientStockException();
        }
    }

    private long saveOrderData(final Member member, final OrderRequest orderRequest) {
        OrderEntity orderEntity = toOrderEntity(member, orderRequest);
        long orderId = orderDao.insert(orderEntity);

        saveOrderItems(orderId, orderRequest);

        return orderId;
    }

    private void saveOrderItems(final long orderId, final OrderRequest orderRequest) {
        List<CartItemInfoRequest> cartItemInfos = orderRequest.getCartItemInfos();

        for (final CartItemInfoRequest cartItemInfo : cartItemInfos) {
            ProductInfoRequest productInfo = cartItemInfo.getProductInfo();

            OrderItemEntity orderItemEntity = toOrderItemEntity(orderId, cartItemInfo, productInfo);
            orderItemDao.insert(orderItemEntity);
        }
    }

    private OrderEntity toOrderEntity(final Member member, final OrderRequest orderRequest) {
        return new OrderEntity(
                CurrentTimeUtil.asString(),
                member.getId(),
                orderRequest.getTotalProductPrice(),
                orderRequest.getTotalDeliveryFee(),
                orderRequest.getUsePoint(),
                orderRequest.getTotalPrice()
        );
    }

    private OrderItemEntity toOrderItemEntity(long orderId, CartItemInfoRequest cartItemInfo, ProductInfoRequest productInfo) {
        return new OrderItemEntity(
                orderId,
                productInfo.getProductId(),
                productInfo.getName(),
                productInfo.getPrice(),
                productInfo.getImageUrl(),
                cartItemInfo.getQuantity()
        );
    }

    public List<OrderResponse> getAllOrders() {
        List<OrderResponse> result = new ArrayList<>();

        List<OrderEntity> orderEntities = orderDao.findAll();
        for (final OrderEntity orderEntity : orderEntities) {
            List<OrderItemEntity> twoOrderItemEntities = orderItemDao.findTwoByOrderId(orderEntity.getId());

            List<OrderItemResponse> orderItems = twoOrderItemEntities.stream()
                    .map(orderItemEntity -> new OrderItemResponse(orderItemEntity.getQuantity(), new ProductResponse(orderItemEntity.getProductId(), orderItemEntity.getProductName(), orderItemEntity.getProductPrice(), orderItemEntity.getProductImageUrl(), productDao.findById(orderItemEntity.getProductId()).getStock())))
                    .collect(Collectors.toList());

            result.add(new OrderResponse(orderEntity.getId(), orderEntity.getCreatedAt(), orderItems));
        }

        return result;
    }
}
