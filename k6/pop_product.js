import http from 'k6/http';
import { sleep, check, group } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export const options = {
    stages: [
        { duration: '10s', target: 10 },
        { duration: '10s', target: 10 },
        { duration: '10s', target: 700 },
        { duration: '10s', target: 10 },
        { duration: '10s', target: 10 },
        { duration: '10s', target: 1000 },
        { duration: '10s', target: 10 },
        { duration: '10s', target: 0 }
    ],
    thresholds: {
        http_req_duration: ['p(99)<1000'], // 99%가 1초 미만
        http_req_failed: ['rate<0.05']     // 실패율 5% 미만
    }
};

const BASE_URL = 'http://127.0.0.1:8080/api/products/popular';

export default function main() {
    group('인기상품조회', () => {
        const res = http.get(BASE_URL, { tags: { name: 'popular-products' } });
        check(res, {
            '조회 성공': (r) => r.status === 200
        });
    });

    sleep(randomIntBetween(1, 3));
}