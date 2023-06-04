package cart.service;

import static cart.domain.Point.validateUsablePoint;
import static java.util.stream.Collectors.toList;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.OrderEntity;
import cart.domain.OrderItemEntity;
import cart.domain.Product;
import cart.dto.CartItemInfoRequest;
import cart.dto.OrderItemResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.ProductInfoRequest;
import cart.exception.MismatchedTotalPriceException;
import cart.exception.MismatchedTotalProductPriceException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.util.CurrentTimeUtil;
import java.util.ArrayList;
import java.util.List;
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
        List<CartItemInfoRequest> cartItemInfos = orderRequest.getCartItems();

        validateOwner(member, cartItemInfos);
        validateTotalProductPrice(cartItemInfos, orderRequest.getTotalProductPrice());
        validateUsablePoint(orderRequest.getUsePoint());
        validateRequestTotalPrice(orderRequest);

        removeCartItem(cartItemInfos);
        savePoint(member, orderRequest.getTotalProductPrice());
        updateProductStock(cartItemInfos);
        return saveOrderData(member, orderRequest);
    }

    private void validateOwner(final Member member, final List<CartItemInfoRequest> cartItemInfos) {
        for (final CartItemInfoRequest cartItemInfo : cartItemInfos) {
            CartItem cartItem = cartItemRepository.findById(cartItemInfo.getCartItemId());

            cartItem.validateOwner(member);
        }
    }

    private void validateTotalProductPrice(final List<CartItemInfoRequest> cartItemInfos, final int totalProductPrice) {
        int expectTotalProductPrice = 0;

        for (final CartItemInfoRequest cartItemInfo : cartItemInfos) {
            ProductInfoRequest productInfo = cartItemInfo.getProduct();

            Product product = productDao.findById(productInfo.getProductId());
            expectTotalProductPrice += product.getPrice() * cartItemInfo.getQuantity();
        }

        if (expectTotalProductPrice != totalProductPrice) {
            throw new MismatchedTotalProductPriceException();
        }
    }

    private void validateRequestTotalPrice(final OrderRequest orderRequest) {
        Integer totalProductPrice = orderRequest.getTotalProductPrice();
        Integer totalDeliveryFee = orderRequest.getTotalDeliveryFee();
        Integer usePoint = orderRequest.getUsePoint();
        Integer totalPrice = orderRequest.getTotalPrice();

        if (totalPrice != (totalProductPrice + totalDeliveryFee - usePoint)) {
            throw new MismatchedTotalPriceException();
        }
    }

    private void removeCartItem(final List<CartItemInfoRequest> cartItemInfos) {
        for (final CartItemInfoRequest cartItemInfo : cartItemInfos) {
            cartItemRepository.deleteById(cartItemInfo.getCartItemId());
        }
    }

    private void savePoint(final Member member, final int totalProductPrice) {
        Member newMember = member.savePoint(totalProductPrice);

        memberRepository.update(newMember);
    }

    private void updateProductStock(final List<CartItemInfoRequest> cartItemInfos) {
        for (final CartItemInfoRequest cartItemInfo : cartItemInfos) {
            ProductInfoRequest productInfo = cartItemInfo.getProduct();
            Product product = productDao.findById(productInfo.getProductId());

            Integer quantity = cartItemInfo.getQuantity();

            productDao.update(product.updateStock(quantity));
        }
    }

    private long saveOrderData(final Member member, final OrderRequest orderRequest) {
        OrderEntity orderEntity = toOrderEntity(member, orderRequest);
        long orderId = orderDao.insert(orderEntity);

        saveOrderItems(orderId, orderRequest);

        return orderId;
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

    private void saveOrderItems(final long orderId, final OrderRequest orderRequest) {
        for (final CartItemInfoRequest cartItemInfo : orderRequest.getCartItems()) {
            ProductInfoRequest productInfo = cartItemInfo.getProduct();

            OrderItemEntity orderItemEntity = toOrderItemEntity(orderId, productInfo, cartItemInfo.getQuantity());
            orderItemDao.insert(orderItemEntity);
        }
    }

    private OrderItemEntity toOrderItemEntity(final long orderId, final ProductInfoRequest productInfo, final int quantity) {
        return new OrderItemEntity(
                orderId,
                productInfo.getProductId(),
                productInfo.getName(),
                productInfo.getPrice(),
                productInfo.getImageUrl(),
                quantity
        );
    }

    public List<OrderResponse> getAllOrderBy(final Long memberId) {
        List<OrderResponse> result = new ArrayList<>();

        for (final OrderEntity orderEntity : orderDao.findAllByMemberId(memberId)) {
            List<OrderItemEntity> twoOrderItemEntities = orderItemDao.findTwoByOrderId(orderEntity.getId());

            List<OrderItemResponse> orderItemResponses = twoOrderItemEntities.stream()
                    .map(orderItemEntity -> OrderItemResponse.of(orderItemEntity, productDao.findById(orderItemEntity.getProductId())))
                    .collect(toList());

            result.add(OrderResponse.of(orderEntity, orderItemResponses));
        }

        return result;
    }

    public OrderResponse findOrderBy(final Long orderId) {
        OrderEntity orderEntity = orderDao.findById(orderId);
        List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderId(orderEntity.getId());

        List<OrderItemResponse> orderItemResponses = orderItemEntities.stream()
                .map(orderItemEntity -> OrderItemResponse.of(orderItemEntity, productDao.findById(orderItemEntity.getProductId())))
                .collect(toList());

        return OrderResponse.of(orderEntity, orderItemResponses);
    }
}
