package cart.application;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.dto.GetDetailedOrderResponse;
import cart.application.dto.GetOrdersRequest;
import cart.application.dto.GetOrdersResponse;
import cart.application.dto.OrderContents;
import cart.application.dto.PostOrderRequest;
import cart.application.dto.SingleKindProductRequest;
import cart.dao.OrderDao;
import cart.dao.PointDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderStatus;
import cart.domain.Page;
import cart.domain.Paginator;
import cart.domain.Point;
import cart.domain.PointCalculator;
import cart.domain.Product;
import cart.domain.QuantityAndProduct;
import cart.exception.AuthenticationException;
import cart.exception.IllegalOrderException;

@Service
public class OrderService {

    private final Paginator<Order> paginator;
    private final PointCalculator pointCalculator;
    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final PointDao pointDao;

    public OrderService(Paginator<Order> paginator, PointCalculator pointCalculator, OrderDao orderDao, ProductDao productDao, PointDao pointDao) {
        this.paginator = paginator;
        this.pointCalculator = pointCalculator;
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.pointDao = pointDao;
    }

    @Transactional(readOnly = true)
    public GetOrdersResponse getOrdersWithPagination(Member member, GetOrdersRequest request) {
        List<Order> orders = orderDao.findAllByMemberId(member.getId());
        Page<Order> paginatedOrders = paginator.paginate(orders, request.getPage());
        List<OrderContents> contents = OrderContents.from(paginatedOrders.getContents());
        return new GetOrdersResponse(paginatedOrders.getTotalPages(), paginatedOrders.getCurrentPage(), paginatedOrders.getPageSize(),
            contents);
    }

    @Transactional(readOnly = true)
    public GetDetailedOrderResponse getOrder(Member member, Long orderId) {
        Order order = orderDao.findById(orderId);
        if (member.equals(order.getMember())) {
            return GetDetailedOrderResponse.of(order);
        }
        throw new AuthenticationException();
    }

    @Transactional
    public Long addOrder(Member member, PostOrderRequest request) {
        // 주문 요청
        // 1. 요청된 사용 포인트 이상을 멤버가 가지는지 검증한다
        Point currentPoint = pointDao.findByMemberId(member.getId());
        int usedPoint = request.getUsedPoint();
        if (currentPoint.isLessThan(usedPoint)) {
            throw new IllegalOrderException("보유한 포인트 이상을 사용할 수 없습니다");
        }
        // 2. 멤버의 포인트를 감액한다
        // 3. 정책에 따른 멤버의 포인트를 증액한다
        List<SingleKindProductRequest> requestProducts = request.getProducts();
        // set으로 처리하는 이유는 프론트에서 같은 상품에 대한 구매 요청이 여러 개 있을 수 있기 때문.
        Set<Long> productIds = requestProducts.stream()
            .map(SingleKindProductRequest::getProductId)
            .collect(toSet());
        List<Product> foundProducts = productDao.getProductsByIds(new ArrayList<>(productIds));
        // 만약 IN으로 가져오는데 요청 중 없는 상품의 id가 포함되었다면? IN으로 가져온 결과의 개수가 요청 product들의 개수와 다를 것.
        if (productIds.size() != foundProducts.size()) {
            throw new IllegalOrderException("존재하지 않는 상품을 주문할 수 없습니다");
        }
        // 지불 총액을 계산한다.
        Map<Long, Product> productsById = foundProducts.stream()
            .collect(toMap(Product::getId, Function.identity()));
        List<QuantityAndProduct> quantityAndProducts = requestProducts.stream()
            .map(requestProduct -> new QuantityAndProduct(requestProduct.getQuantity(),
                productsById.get(requestProduct.getProductId())))
            .collect(toList());
        int totalPrice = quantityAndProducts.stream()
            .mapToInt(q -> q.getQuantity() * q.getProduct().getPrice())
            .sum();
        // 주문 금액보다 포인트를 더 많이 사용할 시 예외가 발생한다
        if (currentPoint.isGreaterThan(totalPrice)) {
            throw new IllegalOrderException("지불할 금액 이상으로 포인트를 사용할 수 없습니다");
        }

        int payAmount = totalPrice - usedPoint;
        int savedPoint = pointCalculator.calculatePoint(payAmount);
        int pointDifference = savedPoint - usedPoint;

        pointDao.update(currentPoint.changeBy(pointDifference));

        // 4. 사용 포인트를 포함한 주문 내역을 등록한다
        return orderDao.save(Order.from(member, payAmount, usedPoint, savedPoint, quantityAndProducts));
    }

    @Transactional
    public void cancelOrder(Member member, Long orderId) {
        // 주문 취소
        Order order = orderDao.findById(orderId);
        // 1. 해당 주문자가 신청한 주문인지 검증한다
        if (order.getMember() != member) {
            throw new IllegalOrderException("올바르지 않은 요청입니다");
        }
        // 2. 주문의 상태가 삭제 가능한지 검증한다
        if (order.getOrderStatus().canNotCancel()) {
            throw new IllegalOrderException(OrderStatus.PENDING.getDisplayName() + " 상태인 주문만 취소가 가능합니다");
        }
        // 3. 포인트를 반환한다
        int savedPoint = order.getSavedPoint();
        int usedPoint = order.getUsedPoint();
        int difference = usedPoint - savedPoint;
        if (difference != 0) {
            Point originalPoint = pointDao.findByMemberId(member.getId());
            Point changedPoint = originalPoint.changeBy(difference);
            pointDao.update(changedPoint);
        }
        // 4. 주문의 상태를 변경한다
        orderDao.update(order.cancel());
    }
}
