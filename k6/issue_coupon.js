import http from 'k6/http';
import { sleep, check, group } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export const options = {
    stages: [
        { duration: '10s', target: 10 },
        { duration: '10s', target: 10 },
        { duration: '10s', target: 700 },  // 급격히 사용자 증가
        { duration: '10s', target: 10 },
        { duration: '10s', target: 10 },
        { duration: '10s', target: 1000 }, // 또 한 번 피크 부하
        { duration: '10s', target: 10 },
        { duration: '10s', target: 0 }
    ],
    thresholds: {
        http_req_duration: ['p(99)<1000'], // 99% 응답이 1초 미만
        http_req_failed: ['rate<0.05']     // 실패율 5% 미만
    }
};

// API 기본 주소
const BASE_URL = 'http://127.0.0.1:8080/api';

// 테스트할 쿠폰 ID (예: 1번 쿠폰)
const COUPON_ID = 1;

export default function main() {
    group('선착순 쿠폰 신청', () => {
        // userId는 가상으로 생성 (동시 접속자 구분용)
        const userId = randomIntBetween(1, 1000000);

        // 엔드포인트 호출
        const url = `${BASE_URL}/coupon/${COUPON_ID}/first-come/users/${userId}`;
        const res = http.get(url, { tags: { name: 'coupon-first-come' } });

        // 응답 체크
        check(res, {
            '응답 200 OK': (r) => r.status === 200,
        });
    });

    // 가상 사용자별 요청 간격
    sleep(randomIntBetween(1, 3));
}
