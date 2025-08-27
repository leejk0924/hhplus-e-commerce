package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.order.application.repository.ThirdParty;
import kr.hhplus.be.server.order.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataPlatFormService {
    private final ThirdParty thirdParty;

    public void sendOrderDataToDataPlatForm(Order order) {
        thirdParty.send(order);
    }
}
