package kr.hhplus.be.server.user.application.repository;

import kr.hhplus.be.server.user.domain.entity.User;

public interface UserPointRepository {
    User loadPoint(Long userId);
    User chargePoint(Long userId, int amount);
}
