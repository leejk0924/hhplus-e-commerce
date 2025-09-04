package kr.hhplus.be.server.coupon.infrastructure.kafka.consumer;

import kr.hhplus.be.infrastructure.kafka.common.KafkaTopics;
import kr.hhplus.be.server.coupon.application.service.UserCouponService;
import kr.hhplus.be.server.coupon.application.service.CouponService;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.infrastructure.kafka.dto.CouponIssueEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueEventHandler {
    private final UserCouponService userCouponService;
    private final CouponService couponService;

    @KafkaListener(
//            topics = "#{T(kr.hhplus.be.infrastructure.kafka.common.KafkaTopics).COUPON_ISSUE.topicName}",
            topics = KafkaTopics.DATA_PLATFORM,
            groupId = "coupon-issue-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleCouponIssueEvent(
            @Payload CouponIssueEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment
    ) {
        try {
            log.info("쿠폰 발급 이벤트 수신. couponId: {}, userId: {}, topic: {}, partition: {}, offset: {}", 
                    event.getCouponId(), event.getUserId(), topic, partition, offset);
            
            // 1. 쿠폰 정보 조회
            Coupon coupon = couponService.hasRemainCoupon(event.getCouponId());
            
            // 2. 중복 발급 검증 (DB 레벨에서 한번 더 체크)
            userCouponService.validNotDuplicate(event.getUserId(), event.getCouponId());
            
            // 3. 쿠폰 발급 처리
            userCouponService.issueCoupon(event.getUserId(), coupon);
            
            // 4. 수동 커밋
            acknowledgment.acknowledge();
            
            log.info("쿠폰 발급 완료. couponId: {}, userId: {}", event.getCouponId(), event.getUserId());
            
        } catch (Exception e) {
            log.error("쿠폰 발급 처리 중 오류 발생. couponId: {}, userId: {}, error: {}", 
                    event.getCouponId(), event.getUserId(), e.getMessage(), e);
            
            // 예외 발생 시 메시지를 재처리하거나 DLQ로 보낼 수 있도록 예외를 다시 던짐
            throw e;
        }
    }
}