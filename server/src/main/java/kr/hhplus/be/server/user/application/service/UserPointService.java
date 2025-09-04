package kr.hhplus.be.server.user.application.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.hhplus.be.server.user.application.dto.PointDto;
import kr.hhplus.be.server.user.application.repository.UserPointRepository;

@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserPointRepository userPointRepository;
    public PointDto loadPoint(Long userId) {
        User user = userPointRepository.loadPoint(userId);
        return PointDto.toDto(user);
    }
    @Transactional
    public PointDto chargePoint(Long userId, int point) {
        User user = userPointRepository.loadPoint(userId);
        user.chargePoint(point);
        return PointDto.toDto(user);
    }
}
