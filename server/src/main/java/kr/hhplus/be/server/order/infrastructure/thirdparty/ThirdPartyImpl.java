package kr.hhplus.be.server.order.infrastructure.thirdparty;

import kr.hhplus.be.infrastructure.kafka.common.Topics;
import kr.hhplus.be.infrastructure.kafka.repository.KafkaRepository;
import kr.hhplus.be.server.order.application.repository.ThirdParty;
import kr.hhplus.be.server.order.domain.entity.Order;
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
        kafkaRepository.sendMessage(Topics.DATA_PLATFORM.getTopicName(), order);
    }
}
