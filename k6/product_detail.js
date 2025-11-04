import http from 'k6/http';
import { sleep, check, group } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export const options = {
    stages: [
        { duration: '10s', target: 10 },
        { duration: '10s', target: 10 },
        { duration: '10s', target: 500 },
        { duration: '10s', target: 10 },
        { duration: '10s', target: 10 },
        { duration: '10s', target: 800 },
        { duration: '10s', target: 10 },
        { duration: '10s', target: 0 }
    ],
    thresholds: {
        http_req_duration: ['p(99)<1000'],
        'checks{name:조회 성공}': ['rate>0.99'],  // 비즈니스 로직 성공률 99% 이상
        'checks{name:응답시간 500ms 미만}': ['rate>0.99']   // 응답시간 체크 99% 이상
    },
    ext: {
        loadimpact: {
            projectID: 0,
            name: "Product Detail Load Test"
        }
    }
};

const BASE_URL = 'http://127.0.0.1:8080/api/products';

// 테스트할 상품 ID 범위 (실제 DB에 있는 상품 ID로 조정 필요)
const PRODUCT_ID_MIN = 1;
const PRODUCT_ID_MAX = 100;

export default function main() {
    group('상품상세조회', () => {
        const productId = randomIntBetween(PRODUCT_ID_MIN, PRODUCT_ID_MAX);
        const res = http.get(`${BASE_URL}/${productId}`, { 
            tags: { name: 'product-detail' } 
        });
        
        check(res, {
            '조회 성공': (r) => r.status === 200,
            '응답 데이터 존재': (r) => r.body && JSON.parse(r.body).productId,
            '응답시간 500ms 미만': (r) => r.timings.duration < 500
        });
    });

    sleep(randomIntBetween(1, 3));
}