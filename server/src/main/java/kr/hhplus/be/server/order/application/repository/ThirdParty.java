package kr.hhplus.be.server.order.application.repository;

import kr.hhplus.be.server.order.domain.entity.Order;

public interface ThirdParty {
    void send(Order order);
}
