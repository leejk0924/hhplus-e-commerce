package kr.hhplus.be.server.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.hhplus.be.server.user.application.dto.BalanceDto;
import kr.hhplus.be.server.user.application.repository.UserPointRepository;
import kr.hhplus.be.server.user.domain.BalancePoint;

@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserPointRepository userPointRepository;
    public BalanceDto loadPoint(Long userId) {
        BalancePoint balancePoint = userPointRepository.loadPoint(userId);
        balancePoint.isValid();
        return BalanceDto.toDto(balancePoint);
    }
    public BalanceDto chargePoint(Long userId, int point) {
        BalancePoint balancePoint = userPointRepository.loadPoint(userId);
        balancePoint.chargePoint(point);
        BalancePoint savedPoint = userPointRepository.savePoint(userId, balancePoint.getBalance());
        return BalanceDto.toDto(savedPoint);
    }
}
