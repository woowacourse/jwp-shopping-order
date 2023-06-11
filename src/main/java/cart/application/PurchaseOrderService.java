package cart.application;

import cart.dao.MemberRewardPointDao;
import cart.dao.PurchaseOrderDao;
import cart.dao.PurchaseOrderItemDao;
import cart.domain.Member;
import cart.domain.Pagination;
import cart.domain.Product;
import cart.domain.point.Point;
import cart.domain.purchaseorder.PurchaseOrderInfo;
import cart.domain.purchaseorder.PurchaseOrderItem;
import cart.dto.product.ProductResponse;
import cart.dto.purchaseorder.response.PurchaseOrderItemInfoResponse;
import cart.dto.purchaseorder.response.PurchaseOrderItemResponse;
import cart.dto.purchaseorder.response.PurchaseOrderPageResponse;
import cart.dto.purchaseorder.response.PurchaseOrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class PurchaseOrderService {

    private final PurchaseOrderDao purchaseOrderDao;
    private final PurchaseOrderItemDao purchaseOrderItemDao;
    private final MemberRewardPointDao memberRewardPointDao;

    public PurchaseOrderService(PurchaseOrderDao purchaseOrderDao,
                                PurchaseOrderItemDao purchaseOrderItemDao,
                                MemberRewardPointDao memberRewardPointDao) {
        this.purchaseOrderDao = purchaseOrderDao;
        this.purchaseOrderItemDao = purchaseOrderItemDao;
        this.memberRewardPointDao = memberRewardPointDao;
    }

    public PurchaseOrderPageResponse getAllByMemberId(Member member, int page) {
        int totalOrders = purchaseOrderDao.getTotalByMemberId(member.getId());
        Pagination pagination = Pagination.create(totalOrders, page);

        List<PurchaseOrderInfo> purchaseOrderInfos = purchaseOrderDao.findMemberByIdWithPagination(member.getId(), pagination);
        List<PurchaseOrderItemInfoResponse> purchaseOrderItemInfoResponses = getPurchaseOrderItemInfoResponse(purchaseOrderInfos);

        return new PurchaseOrderPageResponse(pagination.getTotalPage(), pagination.getCurrentPage(),
                purchaseOrderItemInfoResponses.size(), purchaseOrderItemInfoResponses);
    }

    private List<PurchaseOrderItemInfoResponse> getPurchaseOrderItemInfoResponse(List<PurchaseOrderInfo> purchaseOrderInfos) {
        List<PurchaseOrderItemInfoResponse> purchaseOrderItemInfoResponses = new ArrayList<>();
        for (PurchaseOrderInfo purchaseOrderInfo : purchaseOrderInfos) {
            PurchaseOrderItemInfoResponse purchaseOrderItemInfoResponse = getPurchaseOrderItemInfoResponse(purchaseOrderInfo);
            purchaseOrderItemInfoResponses.add(purchaseOrderItemInfoResponse);
        }
        return purchaseOrderItemInfoResponses;
    }

    private PurchaseOrderItemInfoResponse getPurchaseOrderItemInfoResponse(PurchaseOrderInfo purchaseOrderInfo) {
        List<PurchaseOrderItem> purchaseOrderItems = getPurchaseItems(purchaseOrderInfo.getId());
        Product firstProduct = purchaseOrderItems.get(0).getProduct();
        return new PurchaseOrderItemInfoResponse(
                purchaseOrderInfo.getId(), purchaseOrderInfo.getPayment(), purchaseOrderInfo.getOrderAt(),
                purchaseOrderInfo.getStatus(), firstProduct.getName(), firstProduct.getImageUrl(), purchaseOrderItems.size()
        );
    }

    private List<PurchaseOrderItem> getPurchaseItems(Long purchaseOrderId) {
        return purchaseOrderItemDao.findAllByPurchaseOrderId(purchaseOrderId);
    }

    public PurchaseOrderResponse getPurchaseOrderByOrderId(Long orderId) {
        Optional<PurchaseOrderInfo> purchaseOrderInfoById = purchaseOrderDao.findById(orderId);
        if (purchaseOrderInfoById.isEmpty()) {
            throw new IllegalArgumentException("해당 상품이 존재하지 않습니다.");
        }

        PurchaseOrderInfo purchaseOrderInfo = purchaseOrderInfoById.get();
        List<PurchaseOrderItemResponse> purchaseOrderItemResponses = getPurchaseOrderItemResponses(orderId);
        Point savedPoint = memberRewardPointDao.getPointByOrderId(orderId).orElse(new Point(0, null, null));
        return new PurchaseOrderResponse(orderId, purchaseOrderInfo.getOrderAt(),
                purchaseOrderInfo.getStatus(), purchaseOrderInfo.getPayment(),
                purchaseOrderInfo.getUsedPoint(), savedPoint.calculatePointByExpired(), purchaseOrderItemResponses);
    }

    private List<PurchaseOrderItemResponse> getPurchaseOrderItemResponses(Long orderId) {
        List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderItemDao.findAllByPurchaseOrderId(orderId);
        return purchaseOrderItems.stream()
                                 .map(purchaseOrderItem -> new PurchaseOrderItemResponse(purchaseOrderItem.getQuantity(), ProductResponse.of(purchaseOrderItem.getProduct())))
                                 .collect(Collectors.toList());
    }
}
