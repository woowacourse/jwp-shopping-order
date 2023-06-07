package cart.productpoint.dao;

import cart.init.DBInit;
import cart.productpoint.repository.ProductPointEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class ProductPointDaoTest extends DBInit {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private ProductPointDao productPointDao;
    
    @BeforeEach
    void setUp() {
        productPointDao = new ProductPointDao(jdbcTemplate);
    }
    
    @Test
    void Product의_Point_정보를_저장한다() {
        // given
        final ProductPointEntity productPointEntity = new ProductPointEntity(null, 10.0, true);
        
        // when
        final Long productPointId = productPointDao.createProductPoint(productPointEntity);
        
        // then
        assertThat(productPointId).isPositive();
    }
    
    @Test
    void id로_Product의_Point_정보를_조회한다() {
        // given
        final long id = 1L;
        
        // when
        final ProductPointEntity productPointEntity = productPointDao.getProductPointById(id);
        
        // then
        assertAll(
                () -> assertThat(productPointEntity.getId()).isEqualTo(id),
                () -> assertThat(productPointEntity.getPointRatio()).isEqualTo(10.0),
                () -> assertThat(productPointEntity.isPointAvailable()).isTrue()
        );
    }
    
    @Test
    void Product의_Point_정보를_수정한다() {
        // given
        final ProductPointEntity productPointEntity = new ProductPointEntity(1L, 5.0, false);
        
        // when
        productPointDao.update(productPointEntity);
        
        // then
        final ProductPointEntity resultProductPoint = productPointDao.getProductPointById(1L);
        assertAll(
                () -> assertThat(resultProductPoint.getId()).isOne(),
                () -> assertThat(resultProductPoint.getPointRatio()).isEqualTo(5.0),
                () -> assertThat(resultProductPoint.isPointAvailable()).isFalse()
        );
    }
    
    @Test
    void Product의_Point_정보를_삭제한다() {
        // given
        final long id = 1L;
        
        // expect
        assertThatNoException()
                .isThrownBy(() -> productPointDao.deleteById(id));
    }
}
