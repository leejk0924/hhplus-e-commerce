# 더미 데이터 생성 스크립트

이커머스 프로젝트의 부하 테스트를 위한 더미 데이터를 생성하는 Python 스크립트입니다.

## 📋 생성되는 데이터

| 테이블 | 레코드 수 | 설명 |
|--------|----------|------|
| USERS | 1,000,000 | 사용자 정보 (포인트 잔액 포함) |
| PRODUCTS | 10,000 | 상품 정보 (가격, 재고) |
| COUPON | 100 | 쿠폰 정보 (할인율/금액) |
| USERS_COUPON | ~300,000 | 사용자-쿠폰 연결 (발급/사용/만료) |
| ORDERS | 500,000 | 주문 정보 |
| ORDER_ITEMS | ~1,250,000 | 주문 상세 아이템 |
| POINT_HISTORY | ~2,500,000 | 포인트 충전/사용 내역 |

## 🚀 실행 방법

### 1. Python 스크립트 실행
```bash
cd scripts
python3 generate_test_data.py
```

### 2. 생성된 CSV 파일을 MySQL에 로드

```sql
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE POINT_HISTORY;
TRUNCATE TABLE ORDER_ITEMS;
TRUNCATE TABLE ORDERS;
TRUNCATE TABLE USERS_COUPON;
TRUNCATE TABLE COUPON;
TRUNCATE TABLE PRODUCTS;
TRUNCATE TABLE USERS;
SET FOREIGN_KEY_CHECKS = 1;

LOAD DATA LOCAL INFILE './test_data/users.csv' 
INTO TABLE USERS 
FIELDS TERMINATED BY ',' ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE './test_data/products.csv' 
INTO TABLE PRODUCTS 
FIELDS TERMINATED BY ',' ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE './test_data/coupons.csv' 
INTO TABLE COUPON 
FIELDS TERMINATED BY ',' ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE './test_data/users_coupon.csv' 
INTO TABLE USERS_COUPON 
FIELDS TERMINATED BY ',' ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE './test_data/orders.csv' 
INTO TABLE ORDERS 
FIELDS TERMINATED BY ',' ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE './test_data/order_items.csv' 
INTO TABLE ORDER_ITEMS 
FIELDS TERMINATED BY ',' ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE './test_data/point_history.csv' 
INTO TABLE POINT_HISTORY 
FIELDS TERMINATED BY ',' ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 ROWS;
```

## ⚡ 성능 최적화 팁

### 1. MySQL 설정 (로드 전 실행)
```sql
SET sql_log_bin = 0;
SET autocommit = 0;
SET unique_checks = 0;
SET foreign_key_checks = 0;

SET foreign_key_checks = 1;
SET unique_checks = 1;
SET autocommit = 1;
SET sql_log_bin = 1;
```

### 2. Docker에서 MySQL 메모리 설정
```yaml
services:
  mysql:
    environment:
      - MYSQL_INNODB_BUFFER_POOL_SIZE=2G
      - MYSQL_INNODB_LOG_FILE_SIZE=256M
```

## 📊 예상 파일 크기

- users.csv: ~120MB
- products.csv: ~2MB
- coupons.csv: ~10KB
- users_coupon.csv: ~40MB
- orders.csv: ~80MB
- order_items.csv: ~200MB
- point_history.csv: ~300MB

**총 약 742MB의 CSV 파일이 생성됩니다.**

## 🎯 부하 테스트 활용

생성된 데이터로 다음과 같은 부하 테스트 시나리오를 실행할 수 있습니다:

1. **상품 조회**: 10,000개 상품에 대한 조회 성능
2. **사용자 포인트**: 1,000,000명의 포인트 조회/수정
3. **쿠폰 발급**: 동시 쿠폰 발급 요청 처리
4. **주문 생성**: 대량 주문 데이터 기반 성능 검증
5. **통계 쿼리**: 인기 상품, 매출 집계 등

## ⚠️ 주의사항

1. **디스크 용량**: 최소 2GB 이상의 여유 공간 필요
2. **메모리**: 스크립트 실행 시 1GB 이상 메모리 권장  
3. **실행 시간**: 전체 데이터 생성에 5-10분 소요
4. **MySQL 로드**: 데이터 로드에 10-30분 추가 소요

## 🔧 커스터마이징

스크립트 상단의 설정값을 수정하여 데이터 양을 조절할 수 있습니다:

```python
NUM_USERS = 1_000_000        # 사용자 수
NUM_PRODUCTS = 10_000        # 상품 수  
NUM_COUPONS = 100            # 쿠폰 수
NUM_ORDERS = 500_000         # 주문 수
```