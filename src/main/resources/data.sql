insert IGNORE  into member values (1, 'a@a.com', '1234');
insert IGNORE into member values (2, 'b@b.com', '1234');

insert IGNORE into coupon VALUES (1, '1000원 할인 쿠폰', 'FIXED', 1000);
insert IGNORE into coupon VALUES (2, '2000원 할인 쿠폰', 'FIXED', 2000);
insert IGNORE into coupon VALUES (3, '10% 할인 쿠폰', 'RATE', 10);
insert IGNORE into coupon VALUES (4, '20% 할인 쿠폰', 'RATE', 20);

insert IGNORE into member_coupon values (1, false, 1, 1);
insert IGNORE into member_coupon values (2, false, 1, 1);
insert IGNORE into member_coupon values (3, false, 1, 1);
insert IGNORE into member_coupon values (4, false, 1, 1);

insert IGNORE into member_coupon values (5, false, 1, 2);
insert IGNORE into member_coupon values (6, false, 1, 2);
insert IGNORE into member_coupon values (7, false, 1, 2);
insert IGNORE into member_coupon values (8, false, 1, 2);

insert IGNORE into member_coupon values (9, false, 1, 3);
insert IGNORE into member_coupon values (10, false, 1, 3);
insert IGNORE into member_coupon values (11, false, 1, 3);
insert IGNORE into member_coupon values (12, false, 1, 3);

insert IGNORE into member_coupon values (13, false, 2, 1);
insert IGNORE into member_coupon values (14, false, 2, 1);
insert IGNORE into member_coupon values (15, false, 2, 1);
insert IGNORE into member_coupon values (16, false, 2, 1);

insert IGNORE into member_coupon values (17, false, 2, 2);
insert IGNORE into member_coupon values (18, false, 2, 2);
insert IGNORE into member_coupon values (19, false, 2, 2);
insert IGNORE into member_coupon values (20, false, 2, 2);

insert IGNORE into member_coupon values (21, false, 2, 3);
insert IGNORE into member_coupon values (22, false, 2, 3);
insert IGNORE into member_coupon values (23, false, 2, 3);
insert IGNORE into member_coupon values (24, false, 2, 3);
