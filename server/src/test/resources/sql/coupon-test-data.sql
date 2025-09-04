-- 테스트용 쿠폰 데이터 생성
INSERT INTO coupon (id, name, price, coupon_inventory, created_at, updated_at)
VALUES 
    (1, '테스트 쿠폰 1', 1000, 100, NOW(), NOW()),
    (2, '테스트 쿠폰 2', 2000, 50, NOW(), NOW()),
    (3, '재고 부족 쿠폰', 3000, 1, NOW(), NOW());

-- 테스트용 사용자 데이터가 필요한 경우 추가
-- INSERT INTO users (id, username, created_at, updated_at) VALUES ...