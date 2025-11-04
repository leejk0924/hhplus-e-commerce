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
        'checks{name:응답 성공}': ['rate>0.99'],  // 비즈니스 로직 성공률 99% 이상
        'checks{name:응답시간 OK}': ['rate>0.99']   // 응답시간 체크 99% 이상
    },
    ext: {
        loadimpact: {
            projectID: 0,
            name: "Coupon Issue Load Test"
        }
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
