SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE cart_item RESTART IDENTITY;
TRUNCATE TABLE member RESTART IDENTITY;
TRUNCATE TABLE product RESTART IDENTITY;
TRUNCATE TABLE orders RESTART IDENTITY;
TRUNCATE TABLE orders_item RESTART IDENTITY;
TRUNCATE TABLE point RESTART IDENTITY;
TRUNCATE TABLE point_history RESTART IDENTITY;

SET REFERENTIAL_INTEGRITY TRUE;
