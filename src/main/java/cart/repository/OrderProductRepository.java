package cart.repository;

import cart.dao.OrderProductDao;
import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.OrderProduct;
import cart.domain.OrderProducts;
import cart.domain.Product;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductRepository {
    private final OrderProductDao orderProductDao;

    public OrderProductRepository(final OrderProductDao orderProductDao) {
        this.orderProductDao = orderProductDao;
    }

    public Long save(OrderProduct orderProduct, Long orderId) {
        OrderProductEntity orderProductEntity = toEntity(orderProduct, orderId);

        return orderProductDao.save(orderProductEntity);
    }

    public void saveAll(final List<OrderProduct> orderProducts, final Long orderId) {
        List<OrderProductEntity> orderProductEntities = orderProducts.stream()
                .map(orderProduct -> toEntity(orderProduct, orderId))
                .collect(Collectors.toList());

        orderProductDao.saveAll(orderProductEntities);
    }

    private OrderProductEntity toEntity(OrderProduct orderProduct, Long orderId) {
        Product product = orderProduct.getProduct();
        ProductEntity productEntity = new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );

        return new OrderProductEntity(null, orderId, productEntity, orderProduct.getQuantity());
    }

    public OrderProducts findByOrderId(final Long orderId) {
        List<OrderProductEntity> orderProductEntities = orderProductDao.findByOrderId(orderId);

        return toDomain(orderProductEntities);
    }

    private OrderProducts toDomain(List<OrderProductEntity> orderProductEntities) {
        List<OrderProduct> orderProducts = orderProductEntities.stream()
                .map(orderProductEntity -> {
                    Product product = new Product(
                            orderProductEntity.getProductId(),
                            orderProductEntity.getProductName(),
                            orderProductEntity.getProductPrice(),
                            orderProductEntity.getProductImageUrl()
                    );

                    return new OrderProduct(
                            orderProductEntity.getId(),
                            product,
                            orderProductEntity.getQuantity()
                    );
                })
                .collect(Collectors.toList());

        return new OrderProducts(orderProducts);
    }
}
