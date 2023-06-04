package cart.application;

import cart.dao.PurchaseOrderDao;
import cart.dao.PurchaseOrderItemDao;
import cart.domain.*;
import cart.dto.purchaseorder.PurchaseOrderItemInfoResponse;
import cart.dto.purchaseorder.response.PurchaseOrderPageResponse;
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

    public PurchaseOrderPageResponse getAllByMemberId(Member member, int page) {
        int totalOrders = purchaseOrderDao.getTotalByMemberId(member.getId());
        Pagination pagination = Pagination.create(totalOrders, page);

        List<PurchaseOrderInfo> purchaseOrderInfos = purchaseOrderDao.findMemberByIdWithPagination(member.getId(), pagination);
        List<PurchaseOrderItemInfoResponse> purchaseOrderItemInfoRespons = getPurchaseInfoResponse(purchaseOrderInfos);

        return new PurchaseOrderPageResponse(pagination.getTotalPage(), pagination.getCurrentPage(),
                purchaseOrderItemInfoRespons.size(), purchaseOrderItemInfoRespons);
    }

    private List<PurchaseOrderItemInfoResponse> getPurchaseInfoResponse(List<PurchaseOrderInfo> purchaseOrderInfos) {
        List<PurchaseOrderItemInfoResponse> purchaseOrderItemInfoRespons = new ArrayList<>();
        for (PurchaseOrderInfo purchaseOrderInfo : purchaseOrderInfos) {
            List<PurchaseOrderItem> purchaseOrderItems = getPurchaseItems(purchaseOrderInfo.getId());
            Product firstProduct = purchaseOrderItems.get(0).getProduct();
            PurchaseOrderItemInfoResponse purchaseOrderItemInfoResponse = new PurchaseOrderItemInfoResponse(
                    purchaseOrderInfo.getId(), purchaseOrderInfo.getPayment(), purchaseOrderInfo.getOrderAt(),
                    firstProduct.getName(), firstProduct.getImageUrl(), purchaseOrderItems.size()
            );
            purchaseOrderItemInfoRespons.add(purchaseOrderItemInfoResponse);
        }
        return purchaseOrderItemInfoRespons;
    }

    private List<PurchaseOrderItem> getPurchaseItems(Long purchaseOrderId) {
        return purchaseOrderItemDao.findAllByPurchaseOrderId(purchaseOrderId);
    }
}
