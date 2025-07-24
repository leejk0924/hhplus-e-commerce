package kr.hhplus.be.server.user.application.repository;

import kr.hhplus.be.server.user.domain.BalancePoint;

public interface UserPointRepository {
    BalancePoint loadPoint(Long userId);
}
