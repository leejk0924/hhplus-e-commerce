package kr.hhplus.be.server.coupon.application.facade;

import kr.hhplus.be.infrastructure.kafka.repository.KafkaRepository;
import kr.hhplus.be.server.coupon.application.repository.CouponIssuedRepository;
import kr.hhplus.be.server.coupon.application.service.CouponIssueService;
import kr.hhplus.be.server.coupon.application.service.CouponService;
import kr.hhplus.be.server.coupon.application.service.UserCouponService;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.exception.CouponErrorCode;
import kr.hhplus.be.server.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CouponFacade 단위테스트")
class CouponFacadeUnitTest {

    @Mock
    private UserCouponService userCouponService;
    @Mock
    private CouponService couponService;
    @Mock
    private CouponIssuedRepository couponIssuedRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private KafkaRepository kafkaRepository;
    @Mock
    private CouponIssueService couponIssueService;

    @InjectMocks
    private CouponFacade sut;

    private Long userId;
    private Long couponId;
    private String baseQueueKey;
    private String baseIssuedKey;
    private Coupon testCoupon;

    @BeforeEach
    void setUp() {
        userId = 100L;
        couponId = 1L;
        baseQueueKey = "DB:COUPON:QUEUE";
        baseIssuedKey = "DB:COUPON:ISSUED";
        testCoupon = createTestCoupon(couponId, "테스트 쿠폰", 10);
        
        // @Value 어노테이션으로 주입되는 필드값을 테스트에서 주입
        ReflectionTestUtils.setField(sut, "baseQueueKey", baseQueueKey);
        ReflectionTestUtils.setField(sut, "baseIssuedKey", baseIssuedKey);
    }

    @Test
    @DisplayName("이미 발급된 쿠폰이 있을 경우 - 예외가 발생")
    void enterCouponQueueFromKafka_alreadyIssued_shouldThrowException() {
        // Given
        String expectedIssuedKey = baseIssuedKey + ":" + couponId;
        given(couponIssuedRepository.hasCouponIssued(expectedIssuedKey, userId)).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> sut.enterCouponQueueFromKafka(userId, couponId))
            .isInstanceOf(RestApiException.class)
            .hasFieldOrPropertyWithValue("errorCode", CouponErrorCode.ALREADY_ISSUED);
        
        // 쿠폰 발급 서비스는 호출되지 않아야 함
        verify(couponIssueService, never()).requestCouponIssue(any(), any());
    }

    @Test
    @DisplayName("발급된 쿠폰이 없을 경우 - 쿠폰 발급")
    void enterCouponQueueFromKafka_notIssued_shouldProcessCouponIssueRequest() {
        // Given
        String expectedIssuedKey = baseIssuedKey + ":" + couponId;
        given(couponIssuedRepository.hasCouponIssued(expectedIssuedKey, userId)).willReturn(false);

        // When
        sut.enterCouponQueueFromKafka(userId, couponId);

        // Then
        assertAll(
            () -> verify(couponIssuedRepository).hasCouponIssued(expectedIssuedKey, userId),
            () -> verify(couponIssueService).requestCouponIssue(couponId, userId)
        );
    }

    @Test
    @DisplayName("processCouponIssueKafka - 쿠폰 재고 부족 시 예외 발생")
    void processCouponIssueKafka_noStock_shouldThrowException() {
        // Given
        given(couponService.hasRemainCoupon(couponId))
            .willThrow(new RestApiException(CouponErrorCode.COUPON_OUT_OF_STOCK));

        // When & Then
        assertThatThrownBy(() -> sut.processCouponIssueKafka(couponId, userId))
            .isInstanceOf(RestApiException.class)
            .hasFieldOrPropertyWithValue("errorCode", CouponErrorCode.COUPON_OUT_OF_STOCK);

        // 쿠폰 발급 및 Redis 마킹은 호출되지 않아야 함
        verify(userCouponService, never()).issueCoupon(any(), any());
        verify(couponIssuedRepository, never()).markCouponIssued(any(), any());
    }

    /**
     * 테스트용 쿠폰 객체 생성 헬퍼 메서드
     */
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