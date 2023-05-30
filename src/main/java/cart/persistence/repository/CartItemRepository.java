package cart.persistence.repository;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.persistence.dao.MemberDao;
import cart.persistence.dao.ProductDao;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import static cart.persistence.repository.Mapper.memberToMemberEntityMapper;
import static cart.persistence.repository.Mapper.productEntityToProductMapper;

@Component
public class CartItemRepository {

    private final ProductDao productDao;
    private final MemberDao memberDao;

    public CartItemRepository(final ProductDao productDao, final MemberDao memberDao) {
        this.productDao = productDao;
        this.memberDao = memberDao;
    }

    public Product getProductById(final Long productId) {
        final ProductEntity productEntity = productDao.getProductById(productId);
        return productEntityToProductMapper(productEntity);
    }

    public void updatePoint(final Member member) {
        final MemberEntity memberEntity = memberToMemberEntityMapper(member);
        memberDao.updatePoint(memberEntity);
    }
}
