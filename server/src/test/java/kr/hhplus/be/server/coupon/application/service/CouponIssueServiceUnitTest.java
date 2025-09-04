package kr.hhplus.be.server.coupon.application.service;

import kr.hhplus.be.infrastructure.kafka.common.Topics;
import kr.hhplus.be.infrastructure.kafka.repository.KafkaRepository;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.infrastructure.kafka.dto.CouponIssueEvent;
import kr.hhplus.be.server.coupon.infrastructure.persistence.redis.RedisCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CouponIssueServiceUnitTest {
    @Mock
    private RedisCouponRepository redisCouponRepository;
    @Mock
    private KafkaRepository kafkaRepository;
    @Mock
    private CouponService couponService;

    @InjectMocks
    private CouponIssueService sut;

    private Coupon testCoupon;
    private Long couponId;
    private Long userId;

    @BeforeEach
    void setUp() {
        couponId = 1L;
        userId = 100L;
        testCoupon = createTestCoupon(couponId, "테스트 쿠폰", 10);
    }

    @Test
    @DisplayName("쿠폰 발급 요청 시, Kafka 이벤트를 발행")
    void requestCouponIssue_shouldPublishKafkaEvent() {
        // Given
        given(couponService.hasRemainCoupon(couponId)).willReturn(testCoupon);

        var topicCaptor = forClass(String.class);
        var keyCaptor = forClass(String.class);
        var eventCaptor = forClass(CouponIssueEvent.class);

        // When
        sut.requestCouponIssue(couponId, userId);

        // Then

        verify(kafkaRepository).sendMessage(
                topicCaptor.capture(),
                keyCaptor.capture(),
                eventCaptor.capture()
        );
        CouponIssueEvent capturedEvent = eventCaptor.getValue();

        assertAll(
                () -> assertThat(topicCaptor.getValue()).isEqualTo(Topics.COUPON_ISSUE.getTopicName()),
                () -> assertThat(keyCaptor.getValue()).isEqualTo(String.valueOf(couponId)),
                () -> assertThat(capturedEvent.getCouponId()).isEqualTo(couponId),
                () -> assertThat(capturedEvent.getUserId()).isEqualTo(userId),
                () -> assertThat(capturedEvent.getRequestedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("여러 사용자가 동일한 쿠폰을 요청해도 각각의 이벤트가 발행된다")
    void requestCouponIssue_multipleUsers_shouldPublishSeparateEvents() {
        // Given
        Long userId1 = 100L;
        Long userId2 = 200L;
        given(couponService.hasRemainCoupon(couponId)).willReturn(testCoupon);

        ArgumentCaptor<CouponIssueEvent> eventCaptor = forClass(CouponIssueEvent.class);

        // When
        sut.requestCouponIssue(couponId, userId1);
        sut.requestCouponIssue(couponId, userId2);

        // Then
        verify(kafkaRepository, times(2))
                .sendMessage(anyString(), anyString(), eventCaptor.capture());
        var expected = eventCaptor.getAllValues();

        assertThat(expected).hasSize(2);
        assertThat(expected.get(0).getUserId()).isEqualTo(userId1);
        assertThat(expected.get(1).getUserId()).isEqualTo(userId2);
    }

    private Coupon createTestCoupon(Long id, String couponName, int inventory) {
        return Coupon.of(
                id,
                couponName,
                "AMOUNT",
                1000,
                inventory
        );
    }
}