package cart.dao;

import cart.dao.order.DbOrderCartItemDao;
import cart.dao.order.OrderCartItemDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

class DbOrderCartItemDaoTest {

    private OrderCartItemDao orderCartItemDao;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        orderCartItemDao = new DbOrderCartItemDao(dataSource);
    }

    @Test
    @DisplayName("")
    void insert() {

    }

}