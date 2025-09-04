package kr.hhplus.be.server.order.infrastructure.thirdparty;

import kr.hhplus.be.server.common.kafka.KafkaTopics;
import kr.hhplus.be.server.order.application.repository.ThirdParty;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.infrastructure.persistence.kafka.KafkaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ThirdPartyImpl implements ThirdParty {
    private final KafkaRepository kafkaRepository;
    @Override
    public void send(Order order) {
        kafkaRepository.sendMessage(KafkaTopics.DATA_PLATFORM.getTopicName(), order);
    }
}
