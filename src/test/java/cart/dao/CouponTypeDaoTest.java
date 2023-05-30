package cart.dao;

import cart.domain.CouponType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class CouponTypeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CouponTypeDao couponTypeDao;

    @BeforeEach
    void setUp() {
        this.couponTypeDao = new CouponTypeDao(jdbcTemplate);
    }
    
    @DisplayName("모든 쿠폰 종류를 조회한다.")
    @Test
    void findAll() {
        // when
        List<CouponType> couponTypes = couponTypeDao.findAll();

        // then
        assertAll(
                () -> assertThat(couponTypes).extracting(CouponType::getId)
                        .contains(1L, 2L, 3L, 4L),
                () -> assertThat(couponTypes).extracting(CouponType::getDiscountAmount)
                        .contains(1000, 3000, 5000, 10000),
                () -> assertThat(couponTypes).extracting(CouponType::getName)
                        .contains("할인쿠폰1", "할인쿠폰2", "할인쿠폰3", "할인쿠폰4"),
                () -> assertThat(couponTypes).extracting(CouponType::getDescription)
                        .contains("1000원 할인 쿠폰", "3000원 할인 쿠폰", "5000원 할인 쿠폰", "10000원 할인 쿠폰")
        );
    }

    @DisplayName("아이디로 쿠폰 종류를 조회한다.")
    @Test
    void findById() {
        // when
        CouponType couponType = couponTypeDao.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰 종류입니다. 쿠폰 타입 ID가 일치하는지 확인해주세요."));

        // then
        Assertions.assertAll(
                () -> assertThat(couponType.getId()).isEqualTo(1L),
                () -> assertThat(couponType.getDiscountAmount()).isEqualTo(1000),
                () -> assertThat(couponType.getName()).isEqualTo("할인쿠폰1"),
                () -> assertThat(couponType.getDescription()).isEqualTo("1000원 할인 쿠폰")
        );
    }

}
