INSERT INTO coupon (name, discount_rate, `period`, expired_at)
SELECT '회원가입 감사 쿠폰', 10, 7, '9999-12-31'
WHERE NOT EXISTS (
        SELECT * FROM coupon WHERE name = '회원가입 감사 쿠폰' AND discount_rate = 10
    );
