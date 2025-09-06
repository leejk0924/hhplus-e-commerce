package kr.hhplus.be.infrastructure.kafka.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Topics {
    DATA_PLATFORM("data_platform"),
    COUPON_ISSUE("coupon_issue");
    
    private final String topicName;
}