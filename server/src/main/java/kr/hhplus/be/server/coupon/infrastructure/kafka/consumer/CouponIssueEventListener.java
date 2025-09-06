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
            @Payload CouponIssueEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment
    ) {
        try {
            couponFacade.processCouponIssueKafka(event.getCouponId(), event.getUserId());
            
            // Kafka 수동 커밋
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("쿠폰 발급 처리 중 오류 발생. couponId: {}, userId: {}, error: {}", 
                    event.getCouponId(), event.getUserId(), e.getMessage(), e);
            
            // 예외 발생 시 메시지를 재처리하거나 DLQ로 보낼 수 있도록 예외를 다시 던짐
            throw e;
        }
    }
}