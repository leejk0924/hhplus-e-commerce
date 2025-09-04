-- USERS
INSERT INTO USERS (ID, USER_NAME, POINT_BALANCE, CREATED_AT) VALUES
                                                                 (1, '홍길동', 5000, CURRENT_TIMESTAMP),
                                                                 (2, '이순신', 10000, CURRENT_TIMESTAMP),
                                                                 (3, '김유신', 0, CURRENT_TIMESTAMP);

-- COUPON
INSERT INTO COUPON (ID, COUPON_NAME, DISCOUNT_TYPE, DISCOUNT_RATE, COUPON_INVENTORY, CREATED_AT) VALUES
                                                                                                     (1, 'WELCOME10', 'PERCENT', 10, 100, CURRENT_TIMESTAMP),
                                                                                                     (2, 'SAVE5000', 'AMOUNT', 5000, 50, CURRENT_TIMESTAMP);

-- USERS_COUPON
INSERT INTO USERS_COUPON (ID, USER_ID, COUPON_ID, COUPON_STATUS, REDEEMED_AT, EXPIRED_AT, CREATED_AT) VALUES
                                                                                                          (1, 1, 1, '사용됨', '2025-08-01 12:00:00', '2025-12-31 23:59:59', CURRENT_TIMESTAMP),
                                                                                                          (2, 2, 2, '발급됨', NULL, '2025-12-31 23:59:59', CURRENT_TIMESTAMP);

-- PRODUCTS
INSERT INTO PRODUCTS (ID, PRODUCT_NAME, PRICE, STOCK_QUANTITY, CREATED_AT) VALUES
                                                                               (1, '블루투스 이어폰', 59000, 100, CURRENT_TIMESTAMP),
                                                                               (2, '무선 마우스', 29000, 50, CURRENT_TIMESTAMP),
                                                                               (3, '기계식 키보드', 99000, 30, CURRENT_TIMESTAMP);

-- ORDERS
INSERT INTO ORDERS (ID, USER_ID, USER_COUPON_ID, PRODUCT_TOTAL_AMOUNT, TOTAL_DISCOUNT, PAYMENT_AMOUNT, ORDER_STATUS, PURCHASED_AT, CREATED_AT) VALUES
                                                                                                                                                   (1, 1, 1, 59000, 5900, 53100, '결제완료', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                   (2, 2, NULL, 129000, 0, 129000, '주문완료', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ORDER_ITEMS
INSERT INTO ORDER_ITEMS (ID, ORDER_ID, PRODUCT_ID, QUANTITY, UNIT_PRICE, PURCHASED_AT, CREATED_AT) VALUES
                                                                                                       (1, 1, 1, 1, 59000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                       (2, 2, 2, 1, 29000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                       (3, 2, 3, 1, 99000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- POINT_HISTORY
INSERT INTO POINT_HISTORY (ID, USER_ID, AMOUNT, TRANSACTION_TYPE, CREATED_AT) VALUES
                                                                                  (1, 1, 3000, '포인트 사용', CURRENT_TIMESTAMP),
                                                                                  (2, 2, 5000, '포인트 충전', CURRENT_TIMESTAMP);