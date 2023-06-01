package cart.dao.rowmapper;

import cart.dao.entity.ProductEntity;
import org.springframework.jdbc.core.RowMapper;

public final class ProductRowMapper {

    private ProductRowMapper() {
    }

    public static final RowMapper<ProductEntity> product = (rs, rowNum) -> {
        return new ProductEntity(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("image_url")
        );
    };
}
