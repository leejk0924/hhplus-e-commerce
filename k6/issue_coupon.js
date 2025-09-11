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
        http_req_duration: ['p(99)<1000'],
        http_req_failed: ['rate<0.05']
    }
};

const BASE_URL = 'http://127.0.0.1:8080/api';

const COUPON_ID = 1;

export default function main() {
    group('선착순 쿠폰 신청', () => {
        const userId = randomIntBetween(10000, 50000);

        const url = `${BASE_URL}/coupon/${COUPON_ID}/first-come/users/${userId}`;
        const res = http.get(url, { tags: { name: 'coupon-first-come' } });

        check(res, {
            '응답 성공': (r) => r.status === 200 || r.status === 400,
            '응답시간 OK': (r) => r.timings.duration < 1000,
        });
    });

    sleep(randomIntBetween(1, 3));
}
