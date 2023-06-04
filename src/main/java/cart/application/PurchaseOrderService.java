package cart.application;

import cart.dao.PurchaseOrderDao;
import cart.dao.PurchaseOrderItemDao;
import cart.domain.*;
import cart.dto.PurchaseOrderInfoResponse;
import cart.dto.PurchaseOrderResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderDao purchaseOrderDao;
    private final PurchaseOrderItemDao purchaseOrderItemDao;

    public PurchaseOrderService(PurchaseOrderDao purchaseOrderDao, PurchaseOrderItemDao purchaseOrderItemDao) {
        this.purchaseOrderDao = purchaseOrderDao;
        this.purchaseOrderItemDao = purchaseOrderItemDao;
    }

    public PurchaseOrderResponse getAllByMemberId(Member member, int page) {
        int totalOrders = purchaseOrderDao.getTotalByMemberId(member.getId());
        Pagination pagination = Pagination.create(totalOrders, page);

        List<PurchaseOrderInfo> purchaseOrderInfos = purchaseOrderDao.findMemberByIdWithPagination(member.getId(), pagination);
        List<PurchaseOrderInfoResponse> purchaseOrderInfoResponses = getPurchaseInfoResponse(purchaseOrderInfos);

        return new PurchaseOrderResponse(pagination.getTotalPage(), pagination.getCurrentPage(),
                purchaseOrderInfoResponses.size(), purchaseOrderInfoResponses);
    }

    private List<PurchaseOrderInfoResponse> getPurchaseInfoResponse(List<PurchaseOrderInfo> purchaseOrderInfos) {
        List<PurchaseOrderInfoResponse> purchaseOrderInfoResponses = new ArrayList<>();
        for (PurchaseOrderInfo purchaseOrderInfo : purchaseOrderInfos) {
            List<PurchaseOrderItem> purchaseOrderItems = getPurchaseItems(purchaseOrderInfo.getId());
            Product firstProduct = purchaseOrderItems.get(0).getProduct();
            PurchaseOrderInfoResponse purchaseOrderInfoResponse = new PurchaseOrderInfoResponse(
                    purchaseOrderInfo.getId(), purchaseOrderInfo.getPayment(), purchaseOrderInfo.getOrderAt(),
                    firstProduct.getName(), firstProduct.getImageUrl(), purchaseOrderItems.size()
            );
            purchaseOrderInfoResponses.add(purchaseOrderInfoResponse);
        }
        return purchaseOrderInfoResponses;
    }

    private List<PurchaseOrderItem> getPurchaseItems(Long purchaseOrderId) {
        return purchaseOrderItemDao.findAllByPurchaseOrderId(purchaseOrderId);
    }
}
