#!/usr/bin/env python3
"""
이커머스 더미 데이터 생성 스크립트 - 한꺼번에 실행
"""

import random
from datetime import datetime, timedelta
import mysql.connector
from mysql.connector import Error

# 설정값
NUM_USERS = 50_000
NUM_PRODUCTS = 5_000
NUM_COUPONS = 50
NUM_ORDERS = 20_000

# MySQL 연결 설정
DB_CONFIG = {
    'host': 'localhost',
    'port': 3306,
    'database': 'hhplus',
    'user': 'root',
    'password': 'root',
    'charset': 'utf8mb4',
    'autocommit': False
}

def get_connection():
    return mysql.connector.connect(**DB_CONFIG)

def random_datetime(start_date, end_date):
    time_between = end_date - start_date
    random_days = random.randrange(time_between.days)
    random_seconds = random.randrange(24 * 60 * 60)
    return start_date + timedelta(days=random_days, seconds=random_seconds)

def main():
    print("🚀 더미 데이터 생성 시작!")
    
    connection = get_connection()
    cursor = connection.cursor()
    
    try:
        now = datetime.now()
        base_date = now - timedelta(days=365)
        
        print("🗑️ 기존 데이터 삭제...")
        cursor.execute("SET FOREIGN_KEY_CHECKS = 0")
        cursor.execute("TRUNCATE TABLE POINT_HISTORY")
        cursor.execute("TRUNCATE TABLE ORDER_ITEMS")
        cursor.execute("TRUNCATE TABLE ORDERS")
        cursor.execute("TRUNCATE TABLE USERS_COUPON")
        cursor.execute("TRUNCATE TABLE COUPON")
        cursor.execute("TRUNCATE TABLE PRODUCTS")
        cursor.execute("TRUNCATE TABLE USERS")
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1")
        connection.commit()

        print(f"👥 사용자 {NUM_USERS:,}명 생성 중...")
        users_data = []
        for i in range(1, NUM_USERS + 1):
            user_name = f'User{i:06d}'
            point_balance = random.randint(0, 500000)
            version = 1
            created_at = random_datetime(base_date, now - timedelta(days=30))
            users_data.append((user_name, point_balance, version, created_at, created_at))
        
        cursor.executemany(
            "INSERT INTO USERS (USER_NAME, POINT_BALANCE, VERSION, CREATED_AT, UPDATED_AT) VALUES (%s, %s, %s, %s, %s)",
            users_data
        )
        connection.commit()
        print(f"✅ 사용자 {NUM_USERS:,}명 완료")

        print(f"📦 상품 {NUM_PRODUCTS:,}개 생성 중...")
        categories = ['전자제품', '의류', '도서', '가구', '스포츠', '뷰티', '식품', '완구']
        products_data = []
        for i in range(1, NUM_PRODUCTS + 1):
            category = random.choice(categories)
            product_name = f'{category} 상품 #{i:05d}'
            price = random.randint(1000, 500000)
            stock_quantity = random.randint(10, 500)
            created_at = random_datetime(base_date, now)
            products_data.append((product_name, price, stock_quantity, created_at, created_at))
        
        cursor.executemany(
            "INSERT INTO PRODUCTS (PRODUCT_NAME, PRICE, STOCK_QUANTITY, CREATED_AT, UPDATED_AT) VALUES (%s, %s, %s, %s, %s)",
            products_data
        )
        connection.commit()
        print(f"✅ 상품 {NUM_PRODUCTS:,}개 완료")

        print(f"🎫 쿠폰 {NUM_COUPONS:,}개 생성 중...")
        coupon_names = ['신규가입축하', '생일축하', '재구매감사', '여름특가', '겨울세일', '주말할인', '친구추천', '첫구매']
        coupons_data = []
        for i in range(1, NUM_COUPONS + 1):
            coupon_name = f'{random.choice(coupon_names)} #{i:03d}'
            discount_type = 'AMOUNT'
            discount_rate = random.choice([1000, 3000, 5000, 10000, 15000])
            coupon_inventory = random.randint(1000, 50000)
            created_at = random_datetime(base_date, now)
            coupons_data.append((coupon_name, discount_type, discount_rate, coupon_inventory, created_at, created_at))
        
        cursor.executemany(
            "INSERT INTO COUPON (COUPON_NAME, DISCOUNT_TYPE, DISCOUNT_RATE, COUPON_INVENTORY, CREATED_AT, UPDATED_AT) VALUES (%s, %s, %s, %s, %s, %s)",
            coupons_data
        )
        connection.commit()
        print(f"✅ 쿠폰 {NUM_COUPONS:,}개 완료")

        print("🎟️ 사용자-쿠폰 연결 생성 중...")
        users_coupon_data = []
        statuses = ['발급됨', '사용됨', '만료됨']
        
        for user_id in range(1, NUM_USERS + 1):
            if random.random() < 0.3:
                num_coupons = random.randint(1, 3)
                for _ in range(num_coupons):
                    coupon_id = random.randint(1, NUM_COUPONS)
                    status = random.choice(statuses)
                    created_at = random_datetime(base_date, now - timedelta(days=1))
                    expired_at = created_at + timedelta(days=random.randint(30, 90))
                    redeemed_at = None
                    if status == '사용됨':
                        redeemed_at = random_datetime(created_at, min(expired_at, now))
                    updated_at = redeemed_at if redeemed_at else created_at
                    users_coupon_data.append((user_id, coupon_id, status, redeemed_at, expired_at, created_at, updated_at))
        
        cursor.executemany(
            "INSERT INTO USERS_COUPON (USER_ID, COUPON_ID, COUPON_STATUS, REDEEMED_AT, EXPIRED_AT, CREATED_AT, UPDATED_AT) VALUES (%s, %s, %s, %s, %s, %s, %s)",
            users_coupon_data
        )
        connection.commit()
        print(f"✅ 사용자-쿠폰 {len(users_coupon_data):,}개 완료")

        print(f"🛒 주문 {NUM_ORDERS:,}개 생성 중...")
        orders_data = []
        order_statuses = ['주문완료', '결제완료']
        
        for order_id in range(1, NUM_ORDERS + 1):
            user_id = random.randint(1, NUM_USERS)
            user_coupon_id = None
            if random.random() < 0.2:
                user_coupon_id = random.randint(1, min(len(users_coupon_data), 10000)) if users_coupon_data else None
            
            product_total_amount = random.randint(10000, 300000)
            total_discount = 0
            if user_coupon_id:
                total_discount = min(product_total_amount * 0.1, 15000)
            payment_amount = product_total_amount - total_discount
            order_status = random.choice(order_statuses)
            purchased_at = random_datetime(base_date, now)
            
            orders_data.append((user_id, user_coupon_id, product_total_amount, total_discount, payment_amount, order_status, purchased_at, purchased_at, purchased_at))
        
        cursor.executemany(
            "INSERT INTO ORDERS (USER_ID, USER_COUPON_ID, PRODUCT_TOTAL_AMOUNT, TOTAL_DISCOUNT, PAYMENT_AMOUNT, ORDER_STATUS, PURCHASED_AT, CREATED_AT, UPDATED_AT) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)",
            orders_data
        )
        connection.commit()
        print(f"✅ 주문 {NUM_ORDERS:,}개 완료")

        print("📋 주문 아이템 생성 중...")
        order_items_data = []
        
        for order_id in range(1, NUM_ORDERS + 1):
            num_items = random.randint(1, 3)
            selected_products = random.sample(range(1, NUM_PRODUCTS + 1), num_items)
            
            for product_id in selected_products:
                quantity = random.randint(1, 5)
                unit_price = random.randint(5000, 100000)
                purchased_at = orders_data[order_id - 1][6]
                order_items_data.append((order_id, product_id, quantity, unit_price, purchased_at, purchased_at, purchased_at))
        
        cursor.executemany(
            "INSERT INTO ORDER_ITEMS (ORDER_ID, PRODUCT_ID, QUANTITY, UNIT_PRICE, PURCHASED_AT, CREATED_AT, UPDATED_AT) VALUES (%s, %s, %s, %s, %s, %s, %s)",
            order_items_data
        )
        connection.commit()
        print(f"✅ 주문 아이템 {len(order_items_data):,}개 완료")

        print("💰 포인트 내역 생성 중...")
        point_history_data = []
        transaction_types = ['포인트 충전', '포인트 사용']
        
        for user_id in range(1, NUM_USERS + 1):
            if random.random() < 0.5:
                num_transactions = random.randint(1, 5)
                for _ in range(num_transactions):
                    transaction_type = random.choice(transaction_types)
                    if transaction_type == '포인트 충전':
                        amount = random.choice([10000, 30000, 50000, 100000])
                    else:
                        amount = random.randint(1000, 30000)
                    created_at = random_datetime(base_date, now)
                    point_history_data.append((user_id, amount, transaction_type, created_at, created_at))
        
        cursor.executemany(
            "INSERT INTO POINT_HISTORY (USER_ID, AMOUNT, TRANSACTION_TYPE, CREATED_AT, UPDATED_AT) VALUES (%s, %s, %s, %s, %s)",
            point_history_data
        )
        connection.commit()
        print(f"✅ 포인트 내역 {len(point_history_data):,}개 완료")

        print("\n📊 최종 데이터 확인:")
        tables = ['USERS', 'PRODUCTS', 'COUPON', 'USERS_COUPON', 'ORDERS', 'ORDER_ITEMS', 'POINT_HISTORY']
        for table in tables:
            cursor.execute(f"SELECT COUNT(*) FROM {table}")
            count = cursor.fetchone()[0]
            print(f"  📄 {table}: {count:,}개")
        
        print("\n🎉 모든 더미 데이터 생성 완료!")
        print("💡 이제 K6 부하 테스트를 실행할 수 있습니다!")
        
    except Error as e:
        print(f"❌ 오류 발생: {e}")
        connection.rollback()
    finally:
        cursor.close()
        connection.close()

if __name__ == "__main__":
    print("MySQL 커넥터가 필요합니다: pip install mysql-connector-python")
    print("MySQL이 실행 중인지 확인하세요: docker-compose up -d mysql\n")
    main()