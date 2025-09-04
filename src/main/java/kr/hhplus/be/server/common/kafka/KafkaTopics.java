package kr.hhplus.be.server.common.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KafkaTopics {
    DATA_PLATFORM("data_platform", "데이터 플랫폼 전송 이벤트");
    
    private final String topicName;
    private final String description;
}
