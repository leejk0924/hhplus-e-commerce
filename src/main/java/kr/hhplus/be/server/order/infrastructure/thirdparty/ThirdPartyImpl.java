package kr.hhplus.be.server.order.infrastructure.thirdparty;

import kr.hhplus.be.server.order.application.repository.ThirdParty;
import kr.hhplus.be.server.order.domain.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartyImpl implements ThirdParty {

    @Override
    public void send(Order order) {
        // 데이터 플랙폼에 주문 정보 전송
    }
}
