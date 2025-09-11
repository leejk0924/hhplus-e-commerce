package kr.hhplus.be.server.coupon.infrastructure.kafka.consumer;

import kr.hhplus.be.server.coupon.application.facade.CouponFacade;
import kr.hhplus.be.server.coupon.application.service.CouponService;
import kr.hhplus.be.server.coupon.application.service.UserCouponService;
import kr.hhplus.be.server.coupon.infrastructure.kafka.dto.CouponIssueEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueEventListener {
    private final UserCouponService userCouponService;
    private final CouponService couponService;
    private final CouponFacade couponFacade;

    @KafkaListener(
            topics = "#{T(kr.hhplus.be.infrastructure.kafka.common.Topics).COUPON_ISSUE.topicName}",
            groupId = "#{T(kr.hhplus.be.infrastructure.kafka.common.ConsumerGroups).COUPON_ISSUE.groupId}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleCouponIssueEvent(
            @Payload Map<String, Object> eventMap,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment
    ) {
        try {
            Long couponId = ((Number) eventMap.get("couponId")).longValue();
            Long userId = ((Number) eventMap.get("userId")).longValue();
            log.info("쿠폰 발급 이벤트 처리: couponId={}, userId={}", couponId, userId);
            
            couponFacade.processCouponIssueKafka(couponId, userId);

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("쿠폰 발급 처리 중 오류 발생. eventMap: {}, error: {}", 
                    eventMap, e.getMessage(), e);
            throw e;
        }
    }
}